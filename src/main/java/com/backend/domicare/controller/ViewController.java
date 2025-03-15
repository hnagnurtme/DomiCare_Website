package com.backend.domicare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.backend.domicare.security.jwt.JwtTokenService;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;

@Controller
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
