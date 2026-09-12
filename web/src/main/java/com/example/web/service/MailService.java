package com.example.web.service;

public interface MailService {
    void sendMail(String toEmail, String subject, String content, String body);
}
