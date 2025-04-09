package com.backend.domicare.dto.request;

import java.util.List;

import com.backend.domicare.dto.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddProductRequest {
    private Long categoryId;
    private ProductDTO product;
    private Long mainImageId;
    private List<Long> landingImageIds;
}
