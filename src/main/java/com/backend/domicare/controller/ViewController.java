package com.backend.domicare.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.domicare.security.jwt.JwtTokenService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = {"http://localhost:4000", "http://localhost:4001"})
@RequiredArgsConstructor
public class ViewController {
    private final JwtTokenService authService;


    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam(value ="token") String token , Model model) {
        authService.verifyEmail(token);
        return "ConfirmSuccess";
    }

    @GetMapping("/forgot-password")
    public String  forgotPassword(@RequestParam(value ="token") String token , Model model) {
        String email = authService.verifyEmailAndGetEmail(token);
        model.addAttribute("email", email);
        return "FillPassword";
    }
    
}
