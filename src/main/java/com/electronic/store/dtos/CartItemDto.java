package com.electronic.store.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {


    private int cartItemId;
    @NotBlank
    private int quantity;
    private long totalPrice;
    private ProductDto product;

}
