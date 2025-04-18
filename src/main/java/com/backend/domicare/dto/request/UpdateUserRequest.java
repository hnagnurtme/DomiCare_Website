package com.backend.domicare.dto.request;

import java.time.Instant;

import com.backend.domicare.model.Gender;

import jakarta.validation.constraints.NotNull;
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
public class UpdateUserRequest {
    @NotNull(message = "Không được để trống danh mục")
    private Long userId;
    private String name;
    private String oldPassword;
    private String newPassword;
    private String address;
    private String phone;
    private Gender gender;
    private Instant dateOfBirth;
    private Long imageId;
}
