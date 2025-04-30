package com.electronic.store.Controller;

import com.electronic.store.dtos.AddItemToCartRequest;
import com.electronic.store.dtos.ApiResponseMessage;
import com.electronic.store.dtos.CartDto;
import com.electronic.store.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    public CartService cartService;

    // Add item to cart
    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    @PostMapping("{userId}")
    public ResponseEntity<CartDto> addItemToCart(
            @PathVariable String userId,
            @RequestBody AddItemToCartRequest request
    ){
        CartDto cart = cartService.addItemToCart(userId, request);
        return new ResponseEntity<>(cart, HttpStatus.CREATED);
    }

    //Remove item from cart
    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    @DeleteMapping("{userId}/items/{cartItemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(
            @PathVariable String userId,
            @PathVariable int cartItemId) {
        cartService.removeItemFromCart(userId, cartItemId);
        ApiResponseMessage response= ApiResponseMessage.builder().
                message("Item removed").
                status(HttpStatus.OK).
                success(true).
                build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Clear cart
    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    @DeleteMapping("{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        ApiResponseMessage response= ApiResponseMessage.builder().
                message("Cart cleared").
                status(HttpStatus.OK).
                success(true).
                build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Get cart by user
    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    @GetMapping("{userId}")
    public ResponseEntity<CartDto> getCartByUser(@PathVariable String userId) {
        CartDto cart = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
}
