package com.backend.domicare.exception;

public class InvalidRefreshToken extends RuntimeException {
    public InvalidRefreshToken(String message) {
        super(message);
    }

}
