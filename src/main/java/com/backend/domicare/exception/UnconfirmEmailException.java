package com.backend.domicare.exception;

public class UnconfirmEmailException extends RuntimeException {
    public UnconfirmEmailException(String message) {
        super("Email chưa được xác thực. Vui lòng kiểm tra email của bạn để xác thực tài khoản.");
    }
    
}
