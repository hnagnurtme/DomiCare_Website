package com.backend.domicare.exception;

public class AlreadyRegisterUserException extends RuntimeException{
    public AlreadyRegisterUserException(String s){
        super("Bạn đã đăng kí tài khoản rồi. Vui lòng đăng nhập");
    }
}
