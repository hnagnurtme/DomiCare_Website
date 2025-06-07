package com.backend.domicare.exception;

public class AccountAlreadyDelete extends RuntimeException {
    public AccountAlreadyDelete(String message) {
        super("Tài khoản đã bị xóa, vui lòng kiểm tra lại email hoặc liên hệ với quản trị viên ");
    }
}
