package com.backend.domicare.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.domicare.dto.response.EmailConfirmTokenResponse;
import com.backend.domicare.service.EmailSendingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EmailSendingController {
    private final EmailSendingService sendingEmailService;
    

    @GetMapping("/email/verify")
    public ResponseEntity<Object> sendEmail(@RequestParam(value = "email") String email) {
        String emailToken = this.sendingEmailService.sendEmailFromTemplateSync(email, "Verify your account", "SendingOTP");
        EmailConfirmTokenResponse response = new EmailConfirmTokenResponse(email,emailToken);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/email/reset-password")
    public ResponseEntity<Object> sendResetPasswordEmail(@RequestParam(value = "email") String email) {
        String emailToken = this.sendingEmailService.sendEmailFromTemplateSyncForResetPassword(email, "Reset your password", "ResetPassword");
        EmailConfirmTokenResponse response = new EmailConfirmTokenResponse(email,emailToken);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
