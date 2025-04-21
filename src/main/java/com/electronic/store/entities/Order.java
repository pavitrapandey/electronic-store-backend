package com.electronic.store.entities;

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
@Entity
@Table(name = "orders")
public class Order{
    @Id
    private String orderId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    //PENDING, DISPATCH , DELIVERED, CANCELED
    private String orderStatus;

    //PAID, UNPAID
    private String paymentStatus;

    private double totalAmount;

    private String billingPhone;

    private String billingAddress;

    private String billingName;

    private Date orderDate;

    private Date deliveryDate;

    //COD, Card, UPI
    private String paymentType;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems=new ArrayList<>();
}
