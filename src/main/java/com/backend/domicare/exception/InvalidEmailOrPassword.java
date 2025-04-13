package com.backend.domicare.exception;

public class InvalidEmailOrPassword extends RuntimeException {
    public InvalidEmailOrPassword(String message) {
        super("Email hoặc mật khẩu không đúng. Vui lòng kiểm tra lại.");
    }
    
}
