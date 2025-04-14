package com.backend.domicare.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;

public interface EmailSendingService {
    // Gửi email cơ bản
    void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml);

    // Gửi email từ template bất đồng bộ
    @Async
    CompletableFuture<String> sendEmailFromTemplate(String to, String subject, String templateName, String templateType);
    
    // Gửi email từ template đồng bộ
    String sendEmailFromTemplateSync(String to, String subject, String templateName);
    
    // Gửi email đặt lại mật khẩu từ template đồng bộ
    String sendEmailFromTemplateSyncForResetPassword(String to, String subject, String templateName);

    // Enum để chỉ định loại template (tùy chọn)
    enum TemplateType {
        VERIFICATION, RESET_PASSWORD
    }
}
