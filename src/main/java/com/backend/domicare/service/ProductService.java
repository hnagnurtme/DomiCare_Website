package com.backend.domicare.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.backend.domicare.dto.ProductDTO;
import com.backend.domicare.dto.paging.ResultPagingDTO;
import com.backend.domicare.dto.request.AddProductRequest;
import com.backend.domicare.dto.request.UpdateProductRequest;
import com.backend.domicare.model.Product;

public interface ProductService {
    public ProductDTO addProduct(AddProductRequest addProductRequest);
    public ProductDTO fetchProductById(Long id);
    public void deleteProduct(Long id);
    public ProductDTO updateProduct(UpdateProductRequest productDTO);
    public ResultPagingDTO getAllProducts(Specification<Product> spec,Pageable pageable);
}
