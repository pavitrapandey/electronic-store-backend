package com.electronic.store.dtos;

import lombok.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {

    private String cartId;
    private String userId;
    private String orderStatus="PENDING";
    //PAID, UNPAID
    private String paymentStatus="UNPAID";

    private String billingPhone;

    private String billingAddress;

    private String billingName;

    //COD, Card, UPI
    private String paymentType;

}
