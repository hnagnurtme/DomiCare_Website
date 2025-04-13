package com.backend.domicare.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleForUserRequest {
    @NotNull(message = "Không được để trống id người dùng")
    private Long userId;
    @NotNull(message = "Không được để trống danh sách id quyền")
    private List< Long> roleIds;
}
