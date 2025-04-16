package com.electronic.store.Exception;

import lombok.Builder;

@Builder
public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException() {
        super("Email already exists");
    }

    public EmailAlreadyExistException(String message) {
        super(message);
    }
}
