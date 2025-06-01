package com.backend.domicare.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;

public interface EmailSendingService {
    void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml);

    @Async
    CompletableFuture<String> sendEmailFromTemplate(String to, String subject, String templateName, String templateType);


    @Async
    CompletableFuture<Void> sendPasswordToUser(String to, String fullName,String passWord);


    @Async
    CompletableFuture<Void> sendRejectToUser(String to, String nameService,String dateBooking,String nameUser);
    
    @Async
    CompletableFuture<Void> sendAcceptedToUser(String to, String nameService,String dateBooking,String nameUser);
    

    String sendEmailFromTemplateSync(String to, String subject, String templateName);

    
    String sendEmailFromTemplateSyncForResetPassword(String to, String subject, String templateName);

    enum TemplateType {
        VERIFICATION, RESET_PASSWORD
    }
}
