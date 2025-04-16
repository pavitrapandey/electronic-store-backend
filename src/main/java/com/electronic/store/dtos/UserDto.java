package com.electronic.store.dtos;

import com.electronic.store.Validate.ImageNameValidator;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String userId;

    @Size(min=2,max = 25, message = "invalid name!!!")
    private String name;

//    @Email(message = "email required!!!")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "invalid email!!!")
    @NotBlank(message = "email required!!!")
    private String email;

    @Size(min=3,max = 15, message = "invalid password!!!")
    @NotBlank(message = "password required!!!")
    private String password;

    @Size(min = 4, max=6, message = "invalid gender!!!")
    private String gender;

    @Size(min = 10, max=100, message = "invalid about!!!")
    private String about;

    @ImageNameValidator
    private String imageName;
}
