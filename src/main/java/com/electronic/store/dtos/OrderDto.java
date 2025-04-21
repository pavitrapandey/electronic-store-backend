package com.electronic.store.dtos;

import com.electronic.store.entities.OrderItem;
import com.electronic.store.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderDto {
    private String orderId;

//    private UserDto user;
    //PENDING, DISPATCH , DELIVERED, CANCELED
    private String orderStatus="PENDING";
    //PAID, UNPAID
    private String paymentStatus="UNPAID";

    private double totalAmount;

    private String billingPhone;

    private String billingAddress;

    private String billingName;

    private Date orderDate;

    private Date deliveryDate;
    //COD, Card, UPI
    private String paymentType;

    private List<OrderItemDto> orderItems=new ArrayList<>();
}
