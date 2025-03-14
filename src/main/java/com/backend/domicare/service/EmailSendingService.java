package com.backend.domicare.service;

public interface EmailSendingService {
    public void sendEmail(String to , String subject, String content, boolean isMultiparts, boolean isHtml);
    public String sendEmailFromTemplateSync(String to, String subject, String templateName);
}
