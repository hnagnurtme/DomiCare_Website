package com.backend.domicare.security.dto;

import com.backend.domicare.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private String accessToken;
    private String refreshToken;
    private User user;
}
