package com.mail.resilientEmail.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("providerB")
public  class ProviderB implements EmailProvider{

    private final static Logger logger = LoggerFactory.getLogger(ProviderB.class);

    @Override
    public void send(String to, String subject, String body) {
        if (Math.random() < 0.5) throw new RuntimeException("ProviderB failed");
        logger.info("Provider B has sent email to {}",to);
    }
}
