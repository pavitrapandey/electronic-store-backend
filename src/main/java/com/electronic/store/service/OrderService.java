package com.electronic.store.service;

import com.electronic.store.dtos.CreateOrderRequest;
import com.electronic.store.dtos.OrderDto;
import com.electronic.store.dtos.PageableRespond;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    // create order
    OrderDto createOrder(CreateOrderRequest orderDto);

    // get all orders
   PageableRespond<OrderDto> getAllOrders( Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    // get single order
    List<OrderDto> getOrderById(String orderId);

    // delete order
    void deleteOrder(String userId);

    //update order after purchased
    OrderDto updateOrder(String orderId, CreateOrderRequest request);
}
