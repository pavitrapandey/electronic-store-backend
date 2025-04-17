package com.electronic.store.dtos;

import com.electronic.store.Validate.ImageNameValidator;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDtos {

    private int categoryId;
    @NotBlank
    @Min(value = 3, message = "Title must be at least 3 characters")
    private String title;
    @NotBlank
    private String description;

    @ImageNameValidator
    private String coverImage;
}
