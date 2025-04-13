package com.backend.domicare.exception;

public class RoleAlreadyExists extends RuntimeException {
    public RoleAlreadyExists(String message) {
        super("Vai trò đã tồn tại");
    }
    
}
