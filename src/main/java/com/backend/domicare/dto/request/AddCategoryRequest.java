package com.backend.domicare.dto.request;

import com.backend.domicare.dto.CategoryDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCategoryRequest {
    @NotNull(message = "Không được để trống danh mục")
    private CategoryDTO category;
    @NotNull(message = "Không được để trống ảnh")
    private Long imageId;
}
