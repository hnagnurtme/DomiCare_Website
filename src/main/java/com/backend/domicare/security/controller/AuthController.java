package com.backend.domicare.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.security.dto.LoginRequest;
import com.backend.domicare.security.dto.LoginResponse;
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
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try{
           
            LoginResponse loginResponse = authService.login(loginRequest);
            return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("{\"error\": \"Invalid credentials\"}");
        }
    }

    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) {
        RegisterResponse userResponse = authService.register(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestParam(value ="refresh_token") String refreshToken) {
        String token = authService.createAccessTokenFromRefreshToken(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }
  
    
}
