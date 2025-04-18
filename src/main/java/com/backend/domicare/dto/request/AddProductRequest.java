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
public class AddProductRequest {
    @NotNull(message = "Không được để trống danh mục")
    private Long categoryId;
    @NotNull(message = "Không được để trống tên sản phẩm")
    private String name;
    @NotNull(message = "Không được để trống mô tả")
    private String description;
    @NotNull(message = "Không được để trống giá")
    private double price;
    @NotNull(message = "Không được để trống ảnh")
    private Long mainImageId;
    private Double discount;
    private List<Long> landingImageIds;
}
