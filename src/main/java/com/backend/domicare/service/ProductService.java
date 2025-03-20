package com.backend.domicare.service;

import com.backend.domicare.dto.ProductDTO;

public interface ProductService {
    public ProductDTO addProduct(ProductDTO product);
    public ProductDTO fetchProductById(Long id);
    public void deleteProduct(Long id);
    public ProductDTO updateProduct(Long id, ProductDTO product);
}
