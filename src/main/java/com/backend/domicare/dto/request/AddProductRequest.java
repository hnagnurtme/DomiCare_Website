package com.backend.domicare.dto.request;

import java.util.List;

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
public class AddProductRequest {
    @NotNull(message = "Không được để trống danh mục")
    private Long categoryId;
    @NotNull(message = "Không được để trống sản phẩm")
    private ProductDTO product;
    @NotNull(message = "Không được để trống ảnh")
    private Long mainImageId;
    private List<Long> landingImageIds;
}
