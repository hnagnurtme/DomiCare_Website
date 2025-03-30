package com.backend.domicare.service;

import com.backend.domicare.dto.ProductDTO;
import com.backend.domicare.dto.request.AddProductRequest;
import com.backend.domicare.dto.request.UpdateProductRequest;

public interface ProductService {
    public ProductDTO addProduct(AddProductRequest addProductRequest);
    public ProductDTO fetchProductById(Long id);
    public void deleteProduct(Long id);
    public ProductDTO updateProduct(UpdateProductRequest productDTO);
}
