package com.backend.domicare.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleForUserRequest {
    @NotNull(message = "User ID cannot be empty")
    @Positive(message = "User ID must be positive")
    private Long userId;
    
    @NotNull(message = "Role IDs list cannot be null")
    @NotEmpty(message = "Role IDs list cannot be empty")
    private List<@Positive(message = "Role ID must be positive") Long> roleIds;
}
