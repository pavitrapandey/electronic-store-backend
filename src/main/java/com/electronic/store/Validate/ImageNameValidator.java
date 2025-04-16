package com.electronic.store.Validate;


import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidatorImpl.class)
public @interface ImageNameValidator {

    //error message
    String message() default "Invalid Image Name!!";

    //represents group of constraints
    Class<?>[] groups() default { };

    //represents additional information about annotation
    Class<? extends jakarta.validation.Payload>[] payload() default { };
}
