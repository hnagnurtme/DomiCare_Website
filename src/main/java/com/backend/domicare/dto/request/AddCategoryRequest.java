package com.backend.domicare.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Data transfer object for creating a new category")
public class AddCategoryRequest {
    @NotBlank(message = "Tên danh mục không được để trống")
    @Size(min = 2, max = 100, message = "Tên danh mục phải từ 2-100 ký tự")
    @Schema(description = "Name of the category", example = "Sản phẩm khô", required = true)
    private String name;
    
    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    @Schema(description = "Description of the category", example = "Các sản phẩm khô và đóng gói", required = false)
    private String description;
    
    @NotNull(message = "ID ảnh không được để trống")
    @Schema(description = "ID of the image to be used for the category", example = "1", required = true)
    private Long imageId;
}
