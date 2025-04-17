package com.electronic.store.Exception;

public class BadRequestApiException extends RuntimeException {
    public BadRequestApiException(String message) {
        super(message);

    }
    public BadRequestApiException() {
        super("Bad Request");
    }
}
