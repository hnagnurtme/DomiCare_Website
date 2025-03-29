package com.backend.domicare.security.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotEmpty(message = "{email_not_empty}")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "{email_invalid}")
    @Schema(example = "example@example.com")
    private String email;

    @NotEmpty(message = "{password_not_empty}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$", message = "{password_invalid}")
    @Schema(example = "Password123")
    private String password;

    @NotEmpty(message = "{confirm_password_not_empty}")
    @Schema(example = "Password123")
    private String confirmPassword;
}
