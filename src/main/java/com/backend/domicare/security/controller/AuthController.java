package com.backend.domicare.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.dto.response.Message;
import com.backend.domicare.security.dto.LoginRequest;
import com.backend.domicare.security.dto.LoginResponse;
import com.backend.domicare.security.dto.RefreshTokenRequest;
import com.backend.domicare.security.dto.RefreshTokenRespone;
import com.backend.domicare.security.dto.RegisterResponse;
import com.backend.domicare.security.jwt.JwtTokenService;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
public class AuthController {
    private final JwtTokenService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
            LoginResponse loginResponse = authService.login(loginRequest);
            return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
       
    }

    
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody UserDTO userDTO) {
        RegisterResponse userResponse = authService.register(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenRespone> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshToken) {
        RefreshTokenRespone token = authService.createAccessTokenFromRefreshToken(refreshToken.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Message> verifyEmail(@RequestParam(value ="token") String token) {
        authService.verifyEmail(token);
        return ResponseEntity.status(HttpStatus.OK).body(new Message("Email confirmed successfully"));
    }
  
}
