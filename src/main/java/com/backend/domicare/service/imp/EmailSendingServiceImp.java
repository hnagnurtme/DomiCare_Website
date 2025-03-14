package com.backend.domicare.service.imp;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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


}
