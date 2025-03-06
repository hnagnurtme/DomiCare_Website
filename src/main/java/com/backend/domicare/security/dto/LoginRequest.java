package com.backend.domicare.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class LoginRequest {
    @NotEmpty(message = "Email is required")
    @Email(message = "Email is invalid")
    @Schema(description = "Email must be a valid email address")
    private String email;

    @NotEmpty(message = "Password is required")
    @Schema(description = "Password must contain at least 8 characters, including uppercase, lowercase, and numbers")
    private String password;
}
