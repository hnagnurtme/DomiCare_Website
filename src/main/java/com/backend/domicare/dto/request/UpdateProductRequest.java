package com.backend.domicare.dto.request;

import java.util.List;


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
    
    @NotNull(message = "Không được để trống danh mục cũ")
    private Long oldCategoryId;

    @NotNull(message = "Không được để trống id sản phẩm cũ")
    private Long oldProductId;

    @NotNull(message = "Không được để trống danh mục")
    private Long categoryId;

    private String name;
    private String description;
    private double price; 
    private String mainImageUrl;

    private Double discount;
    
    private List<String> landingImageUrls;

}
