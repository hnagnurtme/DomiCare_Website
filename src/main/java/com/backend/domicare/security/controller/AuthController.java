package com.backend.domicare.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.security.dto.LoginRequest;
import com.backend.domicare.security.dto.LoginResponse;
import com.backend.domicare.security.dto.RefreshTokenRequest;
import com.backend.domicare.security.dto.RefreshTokenRespone;
import com.backend.domicare.security.dto.RegisterRequest;
import com.backend.domicare.security.dto.RegisterResponse;
import com.backend.domicare.security.jwt.JwtTokenService;
import com.backend.domicare.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class AuthController {
    private final JwtTokenService authService;
    private  final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
            LoginResponse loginResponse = authService.login(loginRequest);
            return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
       
    }

    
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody RegisterRequest userDTO) {
        RegisterResponse userResponse = authService.register(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }
  
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenRespone> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshToken) {
        RefreshTokenRespone token = authService.createAccessTokenFromRefreshToken(refreshToken.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @PostMapping("reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody UserDTO userDTO) {
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        userService.resetPassword(email, password);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
