package com.backend.domicare.dto.request;

import com.backend.domicare.dto.ProductDTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequest {
    
    @NotNull(message = "Old category ID cannot be null")
    private Long oldCategoryId;

    @NotNull(message = "Product cannot be null")
    private ProductDTO updateProduct;
}
