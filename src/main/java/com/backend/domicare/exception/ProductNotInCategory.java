package com.backend.domicare.exception;

public class ProductNotInCategory extends RuntimeException {
    public ProductNotInCategory(String message) {
        super(message);
    }
    
}
