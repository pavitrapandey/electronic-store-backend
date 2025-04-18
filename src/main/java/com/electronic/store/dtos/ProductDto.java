package com.electronic.store.dtos;

import com.electronic.store.Validate.ImageNameValidator;
import com.electronic.store.entities.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {


    private String productId;

    @NotBlank
    private String title;
    private String description;

    private long price;

    @ImageNameValidator
    private String productImageName;
    private Date addedDate;
    private boolean live;
    private CategoryDto category;
}
