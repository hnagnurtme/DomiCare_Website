package com.backend.domicare.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data transfer object for updating an existing category")
public class UpdateCategoryRequest {
    @NotNull(message = "ID danh mục không được để trống")
    @Positive(message = "ID danh mục phải là số dương")
    @Schema(description = "ID of the category to update", example = "1", required = true)
    private Long categoryId;
    
    @Size(min = 2, max = 100, message = "Tên danh mục phải từ 2-100 ký tự")
    @Schema(description = "New name for the category", example = "Sản phẩm khô", required = false)
    private String name;
    
    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    @Schema(description = "New description for the category", example = "Các sản phẩm khô và đóng gói", required = false)
    private String description;
    
    @Schema(description = "ID of the new image for the category", example = "http://huhu", required = false)
    private String imageId;
}
