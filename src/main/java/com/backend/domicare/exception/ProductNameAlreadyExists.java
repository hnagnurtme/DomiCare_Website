package com.backend.domicare.exception;

public class ProductNameAlreadyExists extends RuntimeException {
    public ProductNameAlreadyExists(String message) {
        super("Đã tồn tại sản phẩm với tên này");
    }
    
}
