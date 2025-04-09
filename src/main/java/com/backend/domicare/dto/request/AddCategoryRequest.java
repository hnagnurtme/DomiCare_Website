package com.backend.domicare.dto.request;

import com.backend.domicare.dto.CategoryDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCategoryRequest {
    @NotNull(message = "Category cannot be null")
    private CategoryDTO category;
    @NotNull(message = "Image ID cannot be null")
    private Long imageId;
}
