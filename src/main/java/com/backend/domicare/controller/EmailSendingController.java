package com.backend.domicare.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.domicare.service.EmailSendingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EmailSendingController {
    private final EmailSendingService sendingEmailService;
    

    @GetMapping("/email")
    public ResponseEntity<Object> sendEmail(@RequestParam(value = "email") String email) {
        this.sendingEmailService.sendEmailFromTemplateSync(email, "Verify your account", "SendingOTP");
        return ResponseEntity.status(HttpStatus.OK).body("Email sent successfully");
    }
}
