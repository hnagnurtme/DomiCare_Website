package com.backend.domicare.exception;

public class UserNotActiveException  extends RuntimeException {
    public UserNotActiveException(String message) {
        super("Hệ thống đã tự động tạo tài khoản cho bạn, vui lòng check email để xem thông tin tài khoản. ");
    }
    
}
