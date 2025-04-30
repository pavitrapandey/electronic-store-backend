package com.electronic.store.Controller;

import com.electronic.store.dtos.ApiResponseMessage;
import com.electronic.store.dtos.CreateOrderRequest;
import com.electronic.store.dtos.OrderDto;
import com.electronic.store.dtos.PageableRespond;
import com.electronic.store.entities.Order;
import com.electronic.store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/orders")
public class OrderController{

    @Autowired
    private OrderService orderService;

    // create order
    @PreAuthorize("hasRole('NORMAL')")
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest order){
        OrderDto orderResponse = orderService.createOrder(order);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    // get all orders
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<PageableRespond<OrderDto>> getAllOrders(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "orderDate", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        PageableRespond<OrderDto> orderResponse = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }


    // get order of Single user
    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getOrderById(@PathVariable String userId){
        List<OrderDto> orderResponse = orderService.getOrderById(userId);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{orderId}")
    public ResponseEntity<ApiResponseMessage> deleteOrder(@PathVariable String orderId){
        orderService.deleteOrder(orderId);
       ApiResponseMessage response= ApiResponseMessage.builder()
                .message("Order deleted successfully")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // update order after purchased
    @PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
    @PutMapping("{orderId}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable String orderId,
                                                @RequestBody CreateOrderRequest request){
        OrderDto orderResponse = orderService.updateOrder(orderId, request);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }
}
