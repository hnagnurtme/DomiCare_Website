package com.backend.domicare.exception;

public class InvalidRefreshToken extends RuntimeException {
    public InvalidRefreshToken(String message) {
        super("Refresh token không hợp lệ. Vui lòng đăng nhập lại.");
    }

}
