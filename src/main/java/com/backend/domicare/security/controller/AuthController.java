package com.backend.domicare.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.domicare.security.dto.LoginRequest;
import com.backend.domicare.security.dto.LoginResponse;
import com.backend.domicare.security.jwt.JwtTokenManager;
import com.backend.domicare.security.jwt.JwtTokenService;
import com.backend.domicare.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final JwtTokenService authService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try{
            String token = authService.login(loginRequest).getToken();
            return ResponseEntity.ok().body("{\"token\": \"" + token + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("{\"error\": \"Invalid credentials\"}");
        }
    }

}
