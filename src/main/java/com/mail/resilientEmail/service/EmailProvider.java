package com.mail.resilientEmail.service;

public interface EmailProvider {
    void send(String to, String subject, String body);
}
