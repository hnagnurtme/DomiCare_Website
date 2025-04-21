package com.backend.domicare.service.imp;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.backend.domicare.exception.EmailSendingException;
import com.backend.domicare.service.EmailSendingService;
import com.backend.domicare.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class EmailSendingServiceImp implements EmailSendingService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final UserService userService;

    @Override
    public void sendEmail(String to , String subject, String content, boolean isMultiparts, boolean isHtml) {
        this.sendEmailSync(to, subject, content, isMultiparts, isHtml);
    }

    private void sendEmailSync(String to, String subject, String content, boolean isMultiparts, boolean isHtml) {
        MimeMessage message = this.javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, isMultiparts, StandardCharsets.UTF_8.name());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, isHtml);
            this.javaMailSender.send(message);

        } catch (MailException | MessagingException e) {
            throw new EmailSendingException("Failed to send email : " + e);
        }
    }
    
    @Async
    @Override
    public CompletableFuture<String> sendEmailFromTemplate(String to, String subject, String templateName, String templateType) {
        Context context = new Context();
        String verificationToken = this.userService.createVerificationToken(to);
        String encodedVerificationToken = URLEncoder.encode(verificationToken, StandardCharsets.UTF_8);
        context.setVariable("verificationToken", encodedVerificationToken);
        
        // Add email to context if it's a reset password request
        if (templateType.equals(TemplateType.RESET_PASSWORD.name())) {
            context.setVariable("email", to);
        }
        
        String content = templateEngine.process(templateName, context);
        this.sendEmailSync(to, subject, content, false, true);
        return CompletableFuture.completedFuture(encodedVerificationToken);
    }
    
    // Helper methods for internal use - not part of the interface
    @Override
    public String sendEmailFromTemplateSync(String to, String subject, String templateName) {
        Context context = new Context();
        String verificationToken = this.userService.createVerificationToken(to);
        String encodedVerificationToken = URLEncoder.encode(verificationToken, StandardCharsets.UTF_8);
        context.setVariable("verificationToken", encodedVerificationToken);
        String content = templateEngine.process(templateName, context);
        this.sendEmailSync(to, subject, content, false, true);
        return encodedVerificationToken;
    }

    @Override
    public String sendEmailFromTemplateSyncForResetPassword(String to, String subject, String templateName) {
        Context context = new Context();
        String verificationToken = this.userService.createVerificationToken(to);
        String encodedVerificationToken = URLEncoder.encode(verificationToken, StandardCharsets.UTF_8);
        context.setVariable("verificationToken", encodedVerificationToken);
        context.setVariable("email", to);
        String content = templateEngine.process(templateName, context);
        this.sendEmailSync(to, subject, content, false, true);
        return encodedVerificationToken;
    }
    @Override
    @Async
    public CompletableFuture<Void> sendPasswordToUser(String email,String fullName,String passWord){
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("password", passWord);
        context.setVariable("fullName", fullName);
        String templateName = "SendPasswordToUser";
        String content = templateEngine.process(templateName, context);
        String subject = "Cảm ơn bạn đã sử dụng dịch vụ của DomiCare";
        this.sendEmailSync(email, subject, content, false, true);
        return CompletableFuture.completedFuture(null);
    }
}
