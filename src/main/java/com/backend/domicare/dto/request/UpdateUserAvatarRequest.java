package com.backend.domicare.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserAvatarRequest {
    @NotNull(message = "Không được để trống id ảnh")
    private Long imageId;
    @NotNull(message = "Không được để trống id người dùng")
    private Long userId;
}
