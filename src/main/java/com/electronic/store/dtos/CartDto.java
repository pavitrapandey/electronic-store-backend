package com.electronic.store.dtos;


import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {


    private String cartId;
    private Date addedDate;
    private long cartTotal;
    private UserDto user;
    private List<CartItemDto> cartItem=new ArrayList<>();
}
