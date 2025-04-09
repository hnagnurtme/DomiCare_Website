package com.backend.domicare.dto.request;

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
public class AddProductImageRequest {
    private Long productId;
    private Long imageId;
    private Boolean isMainImage;
}
