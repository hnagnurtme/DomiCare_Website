package com.backend.domicare.exception;

public class UserNotActiveException  extends RuntimeException {
    public UserNotActiveException(String message) {
        super("Tài khoản không hoạt động. Vui lòng liên hệ quản trị viên.");
    }
    
}
