package com.mail.resilientEmail.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("providerA")
public class ProviderA implements EmailProvider{

    private final static Logger logger = LoggerFactory.getLogger(ProviderA.class);

    @Override
    public void send(String to, String subject, String body) {
        if (Math.random() < 0.5) throw new RuntimeException("ProviderA failed");
        logger.info("Provider A has sent email to {}",to);
    }
}
