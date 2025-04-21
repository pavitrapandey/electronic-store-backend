package com.electronic.store.service;

import com.electronic.store.dtos.AddItemToCartRequest;
import com.electronic.store.dtos.CartDto;

public interface CartService {
    // add item to cart
    //Case:1 if cart is not present then create a new cart and add the item
    //Case:2 if cart is present then add the item to the cart
    CartDto addItemToCart(String UserId, AddItemToCartRequest request);

    // remove item from cart
    void removeItemFromCart(String UserId, int cartItemId);

    // clear cart
    void clearCart(String UserId);

    // get cart by user
    CartDto getCartByUser(String userId);

    //delete item by quantity
    void deleteItemByQuantity(String userId, int cartItemId, int quantity);
}
