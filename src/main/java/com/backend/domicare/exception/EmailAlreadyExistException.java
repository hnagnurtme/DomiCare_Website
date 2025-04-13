package com.backend.domicare.exception;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException(String message) {
        super("Email này đã được đăng ký trước đó");
    }
    
}
