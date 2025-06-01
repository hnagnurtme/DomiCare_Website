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
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSendingServiceImp implements EmailSendingService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final UserService userService;

    @Override
    public void sendEmail(String to, String subject, String content, boolean isMultiparts, boolean isHtml) {
        this.sendEmailSync(to, subject, content, isMultiparts, isHtml);
    }

    private void sendEmailSync(String to, String subject, String content, boolean isMultiparts, boolean isHtml) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, isMultiparts, StandardCharsets.UTF_8.name());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, isHtml);
            javaMailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (MailException | MessagingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
            throw new EmailSendingException("Failed to send email: " + e.getMessage());
        }
    }

    private String generateVerificationTokenContext(Context context, String to) {
        String verificationToken = userService.createVerificationToken(to);
        String encodedVerificationToken = URLEncoder.encode(verificationToken, StandardCharsets.UTF_8);
        context.setVariable("verificationToken", encodedVerificationToken);
        return encodedVerificationToken;
    }

    private String buildEmailContent(String templateName, Context context) {
        return templateEngine.process(templateName, context);
    }

    @Async("taskExecutor")
    @Override
    public CompletableFuture<String> sendEmailFromTemplate(String to, String subject, String templateName, String templateType) {
        try {
            Context context = new Context();
            String encodedVerificationToken = generateVerificationTokenContext(context, to);

            if ("RESET_PASSWORD".equalsIgnoreCase(templateType)) {
                context.setVariable("email", to);
            }

            String content = buildEmailContent(templateName, context);
            sendEmailSync(to, subject, content, false, true);
            return CompletableFuture.completedFuture(encodedVerificationToken);
        } catch (Exception e) {
            log.error("Error sending email from template: {}", e.getMessage(), e);
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    public String sendEmailFromTemplateSync(String to, String subject, String templateName) {
        Context context = new Context();
        String encodedVerificationToken = generateVerificationTokenContext(context, to);
        String content = buildEmailContent(templateName, context);
        sendEmailSync(to, subject, content, false, true);
        return encodedVerificationToken;
    }

    @Override
    public String sendEmailFromTemplateSyncForResetPassword(String to, String subject, String templateName) {
        Context context = new Context();
        String encodedVerificationToken = generateVerificationTokenContext(context, to);
        context.setVariable("email", to);
        String content = buildEmailContent(templateName, context);
        sendEmailSync(to, subject, content, false, true);
        return encodedVerificationToken;
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Void> sendPasswordToUser(String email, String fullName, String password) {
        try {
            Context context = new Context();
            context.setVariable("email", email);
            context.setVariable("password", password);
            context.setVariable("fullName", fullName);
            String content = buildEmailContent("SendPasswordToUser", context);
            sendEmailSync(email, "Cảm ơn bạn đã sử dụng dịch vụ của DomiCare", content, false, true);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            log.error("Failed to send password to user: {}", email, e);
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> sendRejectToUser(String to, String nameService, String dateBooking, String nameUser) {
        try {
            Context context = new Context();
            context.setVariable("nameUser", nameUser);
            context.setVariable("nameService", nameService);
            context.setVariable("dateBooking", dateBooking);
            context.setVariable("reason", "Hệ thống đang gặp sự cố");
            String content = buildEmailContent("SendRejectBooking", context);
            sendEmailSync(to, "Yêu cầu dịch vụ của bạn đã bị từ chối", content, false, true);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            log.error("Failed to send rejection email: {}", to, e);
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> sendAcceptedToUser(String to, String nameService, String dateBooking, String nameUser) {
        try {
            Context context = new Context();
            context.setVariable("nameUser", nameUser);
            context.setVariable("nameService", nameService);
            context.setVariable("dateBooking", dateBooking);
            String content = buildEmailContent("SendAcceptedBooking", context);
            sendEmailSync(to, "Yêu cầu dịch vụ của bạn đã được chấp nhận", content, false, true);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            log.error("Failed to send acceptance email: {}", to, e);
            return CompletableFuture.failedFuture(e);
        }
    }
}
