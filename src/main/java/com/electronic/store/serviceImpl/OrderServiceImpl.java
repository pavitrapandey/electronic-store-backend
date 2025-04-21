package com.electronic.store.serviceImpl;

import com.electronic.store.Exception.BadRequestApiException;
import com.electronic.store.Exception.ResourceNotFoundException;
import com.electronic.store.Repository.CartRepository;
import com.electronic.store.Repository.OrderRepository;
import com.electronic.store.Repository.UserRepository;
import com.electronic.store.dtos.CreateOrderRequest;
import com.electronic.store.dtos.OrderDto;
import com.electronic.store.dtos.PageableRespond;
import com.electronic.store.entities.*;
import com.electronic.store.helper.Helper;
import com.electronic.store.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;


    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {
        User user = userRepository.findById(orderDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = cartRepository.findById(orderDto.getCartId()).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        List<CartItem> cartItems = cart.getCartItem();

        if(cartItems.isEmpty()) {
            throw new BadRequestApiException("Cart is empty");
        }

        Order order=Order.builder()
                .billingAddress(orderDto.getBillingAddress())
                .billingName(orderDto.getBillingName())
                .orderId(UUID.randomUUID().toString())
                .billingPhone(orderDto.getBillingPhone())
                .paymentType(orderDto.getPaymentType())
                .orderDate(new Date())
                .orderStatus(orderDto.getOrderStatus()) // Use the value from CreateOrderRequest
                .paymentStatus(orderDto.getPaymentStatus())
                .user(user)
                .build();

        AtomicReference<Long> orderAmount=new AtomicReference<>(0L);
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getTotalPrice() * cartItem.getQuantity())
                    .build();
            orderItem.setOrder(order);

            orderAmount.set(orderAmount.get()+orderItem.getTotalPrice());
            return orderItem ;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        order.setTotalAmount(orderAmount.get());

        cart.getCartItem().clear();
        cart.setCartTotal(0);
        cartRepository.save(cart);
       Order saved= orderRepository.save(order);

        return mapper.map(saved,OrderDto.class) ;

    }

    @Override
    public PageableRespond<OrderDto> getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        // Create sort object
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        // Fetch orders
        Page<Order> page = orderRepository.findAll(pageable);
        // Prepare response
        PageableRespond<OrderDto> response = Helper.getPageableResponse(page, OrderDto.class);
        return response;
    }


    @Override
    public List<OrderDto> getOrderById(String userId){
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("Invalid userId "));
        List<Order> order=orderRepository.findByUser(user);
       List<OrderDto> orderDtos= order.stream()
               .map(order1 -> mapper.map(order1,OrderDto.class))
               .collect(Collectors.toList());
        return orderDtos;
    }



    @Override
    public void deleteOrder(String orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Order not found"));
        orderRepository.delete(order);
    }

    @Override
    public OrderDto updateOrder(String orderId, CreateOrderRequest request){
        String orderStatus=request.getOrderStatus();
        String paymentStatus=request.getPaymentStatus();
        Order order=orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Order not found"));
        order.setOrderStatus(orderStatus);
        order.setPaymentStatus(paymentStatus);
        order.setDeliveryDate(new Date());

        Order updatedOrder=orderRepository.save(order);
        return mapper.map(updatedOrder,OrderDto.class);
    }
}
