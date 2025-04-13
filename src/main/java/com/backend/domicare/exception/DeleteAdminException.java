package com.backend.domicare.exception;

public class DeleteAdminException extends RuntimeException {
    public DeleteAdminException(String message) {
        super("Không thể xoá tài khoản admin");
    }
    
}
