package com.backend.domicare.exception;

public class NotFoundRoleException extends RuntimeException {
    public NotFoundRoleException(String message) {
        super("Không tìm thấy thông tin về quyền này");
    }
    
}
