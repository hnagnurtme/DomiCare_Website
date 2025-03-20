package com.backend.domicare.service;

import com.backend.domicare.dto.ProductDTO;

public interface ProductService {
    public void addProduct(ProductDTO product);
    public void deleteProduct(Long id);
    public void updateProduct(Long id, ProductDTO product);
    public void addProductToCategory(Long productId, Long categoryId);
    public void removeProductFromCategory(Long productId, Long categoryId);
}
