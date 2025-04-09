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

import com.backend.domicare.dto.ProductDTO;
import com.backend.domicare.dto.request.AddProductImageRequest;
import com.backend.domicare.dto.request.AddProductRequest;
import com.backend.domicare.dto.request.UpdateProductRequest;
import com.backend.domicare.dto.response.Message;
import com.backend.domicare.model.Product;
import com.backend.domicare.service.ProductService;
import com.turkraft.springfilter.boot.Filter;

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
            @RequestParam(defaultValue = "0") Long categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection,
            @Filter Specification<Product> spec, Pageable pageable) {

        
        // Nếu có sortByStar, sử dụng phương thức sortProductByStar
        if (sortBy != null && sortBy.equals("ratingStar")) {
            Boolean sortByStar = true;
            Boolean isAcsending = true;
            if (sortDirection.equalsIgnoreCase("asc")) {
                isAcsending = true;
            } else if (sortDirection.equalsIgnoreCase("desc")) {
                isAcsending = false;
            }
            return ResponseEntity.status(HttpStatus.OK)
                .body(productService.sortProductByStar(spec, pageable, sortByStar, isAcsending));
        }
        if (sortBy != null && !sortBy.isEmpty()) {
            // Xử lý sắp xếp theo tham số sortBy và sortDirection
            Sort sort = Sort
                    .by(sortDirection.equalsIgnoreCase("desc") ? Sort.Order.desc(sortBy) : Sort.Order.asc(sortBy));
            pageable = PageRequest.of(page - 1, size, sort); // Cập nhật pageable với sort
        } else {
            pageable = PageRequest.of(page - 1, size); // Không có sắp xếp, chỉ phân trang
        }

        
        // Nếu có categoryId, sử dụng phương thức getAllProductsInCategory
        if (categoryId > 0) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(productService.getAllProductsInCategory(spec, pageable, (long) categoryId));
        }
        // Use the specification and pageable to fetch products
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts(spec, pageable));
    }

    @PutMapping("/products/images")
    public ResponseEntity<?> uploadProductImage(@RequestBody AddProductImageRequest addProductRequest) {
        ProductDTO productDTO = this.productService.addProductImage(addProductRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
    }
}
