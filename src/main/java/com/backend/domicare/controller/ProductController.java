package com.backend.domicare.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.domicare.dto.ProductDTO;
import com.backend.domicare.dto.request.AddProductRequest;
import com.backend.domicare.dto.request.UpdateProductRequest;
import com.backend.domicare.dto.response.Message;
import com.backend.domicare.model.Product;
import com.backend.domicare.service.ProductService;
import com.backend.domicare.utils.QueryAdvance;

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

    // Fetch all products with pagination and sorting
    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts(
        @RequestParam(required = false) String searchName,
        @RequestParam(required = false) String otherSearch,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "ASC") String direction,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
            // Validate input parameters
            if (page < 0) {
                return ResponseEntity.badRequest().body("Page number cannot be negative");
            }
            if (size <= 0) {
                return ResponseEntity.badRequest().body("Page size must be greater than 0");
            }

            // Create Sort object with error handling
            Sort sort;
            try {
                sort = direction.equalsIgnoreCase("ASC")
                    ? Sort.by(sortBy).ascending() 
                    : Sort.by(sortBy).descending();
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid sort field: " + sortBy);
            }

            // Create Pageable object
            Pageable pageable = PageRequest.of(page, size, sort);

            // Initialize Specification
            Specification<Product> spec = Specification.where(null);

            // Add search conditions
            if (searchName != null && !searchName.trim().isEmpty()) {
                spec = spec.and((root, query, cb) -> 
                    cb.like(cb.lower(root.get("name")), "%" + searchName.trim().toLowerCase() + "%"));
            }

            if (otherSearch != null && !otherSearch.trim().isEmpty()) {
                spec = spec.and((root, query, cb) ->
                    QueryAdvance.buildDynamicPredicate(otherSearch.trim(), root, query, cb));
                
            }

            // Get products and return response
            return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts(spec, pageable));
    }

    
    

    @PostMapping("/products/image/{id}")
    public ResponseEntity<?> uploadProductImage(@PathVariable Long id, @RequestBody MultipartFile image) {
        ProductDTO productDTO = this.productService.addProductImage(id, image);
        return ResponseEntity.status(HttpStatus.OK).body(productDTO);
    }
}
