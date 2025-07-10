package com.mail.resilientEmail.controller;

import com.mail.resilientEmail.DTO.EmailRequest;
import org.springframework.http.ResponseEntity;
import com.mail.resilientEmail.service.EmailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService){
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public String sendMail(@RequestBody EmailRequest emailRequest){
        String status = emailService.sendMail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
        return status;
    }

    @PostMapping("/status")
    public String getStatus(@RequestBody EmailRequest emailRequest) {
        String status = emailService.getStatus(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
        return status;
    }
}
