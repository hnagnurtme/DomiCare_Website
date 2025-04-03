package com.backend.domicare.exception;

public class RoleAlreadyExists extends RuntimeException {
    public RoleAlreadyExists(String message) {
        super(message);
    }
    
}
