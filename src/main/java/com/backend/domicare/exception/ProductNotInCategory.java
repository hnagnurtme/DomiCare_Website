package com.backend.domicare.exception;

public class ProductNotInCategory extends RuntimeException {
    public ProductNotInCategory(String message) {
        super("San phẩm không thuộc danh mục này");
    }
    
}
