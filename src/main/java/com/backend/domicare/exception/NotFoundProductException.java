package com.backend.domicare.exception;

public class NotFoundProductException extends RuntimeException {
    public NotFoundProductException(String message) {
        super(message);
    }
}
