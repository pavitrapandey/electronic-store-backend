package com.electronic.store.dtos;

import com.electronic.store.Validate.ImageNameValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String userId;

    @Size(min=2,max = 25, message = "invalid name!!!")
    @Schema(name = "User_name", description = "Name of the user")
    private String name;

//    @Email(message = "email required!!!")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "invalid email!!!")
    @NotBlank(message = "email required!!!")
    @Schema(name = "User_email", description = "Email of the user")
    private String email;

    @Size(min=3,max = 15, message = "invalid password!!!")
    @NotBlank(message = "password required!!!")
    @Schema(name = "User_password", description = "Password of the user")
    private String password;

    @Size(min = 4, max=6, message = "invalid gender!!!")
    @Schema(name = "User",description = "Gender if user wanted to share")
    private String gender;

    @Size(min = 10, max=100, message = "invalid about!!!")
    @Schema(name = "User_about", description = "About the user")
    private String about;

    @ImageNameValidator
    @Schema(name = "User_imageName", description = "Image name of the user")
    private String imageName;

    private List<RoleDto> roles;
}
