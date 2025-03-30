package com.backend.domicare.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.domicare.dto.ProductDTO;
import com.backend.domicare.dto.request.AddProductRequest;
import com.backend.domicare.dto.request.UpdateProductRequest;
import com.backend.domicare.dto.response.Message;
import com.backend.domicare.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;

    // Add product
    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@Valid @RequestBody AddProductRequest addProductRequest) {
       
        ProductDTO product = productService.addProduct(addProductRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }


    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Message("Product deleted successfully"));
    }


    // Fetch product by id
    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.fetchProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }


    // Update product
    @PutMapping("/products")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody UpdateProductRequest productDTO) {
        
        ProductDTO updatedProduct = productService.updateProduct(productDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }
}
