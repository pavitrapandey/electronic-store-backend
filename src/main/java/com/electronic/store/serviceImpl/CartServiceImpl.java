package com.electronic.store.serviceImpl;

import com.electronic.store.Exception.BadRequestApiException;
import com.electronic.store.Exception.ResourceNotFoundException;
import com.electronic.store.Repository.CartItemRepository;
import com.electronic.store.Repository.CartRepository;
import com.electronic.store.Repository.ProductRepository;
import com.electronic.store.Repository.UserRepository;
import com.electronic.store.dtos.AddItemToCartRequest;
import com.electronic.store.dtos.CartDto;
import com.electronic.store.entities.Cart;
import com.electronic.store.entities.CartItem;
import com.electronic.store.entities.Product;
import com.electronic.store.entities.User;
import com.electronic.store.service.CartService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CartItemRepository cartItemRepository;

    // add item to cart
    @Override
    public CartDto addItemToCart(String UserId, AddItemToCartRequest request){
        int quantity=request.getQuantity();
        if(quantity<=0){
            throw new BadRequestApiException("Required quantity is not valid!!!");
        }
        //Find product
        Product product=productRepository.findById(request.getProductId()).orElseThrow(()->new ResourceNotFoundException("Product not Avialable"));
        //Find user
        User user=userRepository.findById(UserId).orElseThrow(()->new ResourceNotFoundException("User not Avialable"));
        //Find cart
        Cart cart=null;
        try {
            cart=cartRepository.findByUser(user).get();
        } catch (NoSuchElementException e) {
            cart=new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setAddedDate(new Date());
        }
        //Create cart item
        //if item is already present in the cart then update the quantity and total price
        AtomicReference<Boolean> isPresent= new AtomicReference<>(false);
        List<CartItem> items = cart.getCartItem();
        for (CartItem item : items) {
            if (item.getProduct().getProductId().equals(request.getProductId())) {
                item.setQuantity(item.getQuantity() + quantity);
                item.setTotalPrice(item.getTotalPrice() + quantity * product.getPrice());
                isPresent.set(true);
                break;
            }
        }

        //If item is not present in the cart then add the item to the cart
        if(!isPresent.get()){
            CartItem cartItem=CartItem.builder().product(product).
                    quantity(quantity).
                    totalPrice(quantity*product.getPrice()).
                    cart(cart).
                    build();
            cart.getCartItem().add(cartItem);
        }
        //Add cart item to cart

        cart.setUser(user);
        // Calculate and set total cart amount
        long totalCartAmount = cart.getCartItem().stream()
                .mapToLong(CartItem::getTotalPrice)
                .sum();
        cart.setCartTotal(totalCartAmount);
        Cart updatedCart=cartRepository.save(cart);
        return mapper.map(updatedCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart Item not Available"));

        Cart cart = cartItem.getCart();


        cart.getCartItem().removeIf(item -> item.getCartItemId() == cartItemId);

        // Update the total amount
        long updatedTotal = cart.getCartItem().stream()
                .mapToLong(CartItem::getTotalPrice)
                .sum();
        cart.setCartTotal(updatedTotal);

        // Save the cart
        cartRepository.save(cart);

        // Finally delete the cartItem
        cartItemRepository.delete(cartItem);
    }

    @Override
    @Transactional
    public void clearCart(String userId) {
        // Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not available"));

        // Find cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not available"));

        // Clear cart items
        List<CartItem> items = cart.getCartItem();

        // Explicitly remove each item to ensure deletion
        for (CartItem item : items) {
            item.setCart(null); // break relation
        }

        cart.getCartItem().clear();

        cart.setCartTotal(0); // reset cart total
        cartRepository.save(cart);
    }


    @Override
    public CartDto getCartByUser(String userId) {
        //Find user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not Avialable"));
        //Find cart
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not Avialable"));

        return mapper.map(cart, CartDto.class);
    }


}
