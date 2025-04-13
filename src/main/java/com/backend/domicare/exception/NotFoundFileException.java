package com.backend.domicare.exception;

public class NotFoundFileException extends RuntimeException {
    public NotFoundFileException(String message) {
        super("Không tìm thấy thông tin về file này");
    }
}
