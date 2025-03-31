package com.backend.domicare.exception;

public class ProductNameAlreadyExists extends RuntimeException {
    public ProductNameAlreadyExists(String message) {
        super(message);
    }
    
}
