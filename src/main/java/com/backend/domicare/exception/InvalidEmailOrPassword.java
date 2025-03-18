package com.backend.domicare.exception;

public class InvalidEmailOrPassword extends RuntimeException {
    public InvalidEmailOrPassword(String message) {
        super(message);
    }
    
}
