package com.electronic.store.Exception;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CategoryAlreadyExistException extends RuntimeException {

    public CategoryAlreadyExistException(String message) {
        super(message);
    }

    public CategoryAlreadyExistException() {
        super("Category already exists");
    }


}
