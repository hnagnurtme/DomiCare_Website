package com.backend.domicare.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCategoryRequest {
    @NotNull(message = "Không được để trống tên")
    private String name;
    private String description;
    @NotNull(message = "Không được để trống ảnh")
    private Long imageId;
}
