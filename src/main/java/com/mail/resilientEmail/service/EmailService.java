package com.mail.resilientEmail.service;

import com.mail.resilientEmail.exception.DuplicateEmailException;
import com.mail.resilientEmail.exception.RateLimitExceededException;
import com.mail.resilientEmail.exception.UnableToSendMailException;
import com.mail.resilientEmail.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmailService {
    private final EmailProvider providerA;
    private final EmailProvider providerB;
    private final int maxRetry = 3;
    private final long backOff = 500;

    private final Set<String> sentEmails = new HashSet<>();
    private final Map<String, ArrayList<Long>> rateLimitMap = new HashMap<>();
    private final Map<String, String> statusMap = new HashMap<>();
    private final static Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    public EmailService(@Qualifier("providerA") ProviderA providerA,@Qualifier("providerB") ProviderB providerB) {
        this.providerA = providerA;
        this.providerB = providerB;
    }

    public String sendMail(String to, String subject, String body) {
        String key = Utility.makeKey(to, subject, body);
        if (sentEmails.contains(key)) {
            String temp = statusMap.get(key);
            temp += "duplicate\n";
            statusMap.put(key, temp);
            throw new DuplicateEmailException("This mail is already sent");
        }
        if (!Utility.canSend(to,rateLimitMap)) {
            String temp = "";
            if(statusMap.containsKey(key)) temp = statusMap.get(key);
            temp += "rate_limited\n";
            statusMap.put(key, temp);
            throw new RateLimitExceededException("You have exceeded the limit");
        }

        boolean sent = false;
        String status = "";
        EmailProvider[] providers = {providerA, providerB};
        for (int p = 0; p < providers.length && !sent; p++) {
            logger.info("Currently we are with provider {}", p==0?"A":"B");

            for (int i = 1; i <= maxRetry; i++) {
                try {
                    providers[p].send(to, subject, body);
                    sent = true;
                    sentEmails.add(key);
                    status += "Sent by provider " + (p == 0 ? "A" : "B") + " on try " + i+"\n";
                    statusMap.put(key, status);
                    status = "Sent by provider " + (p == 0 ? "A" : "B") + " on try " + i+"\n";
                    break;
                } catch (Exception e) {
                    status += "Provider " + (p == 0 ? "A" : "B") + " failed try " + i + ". \n";
                    try {
                        Thread.sleep(backOff * (1L << (i - 1)));
                    } catch (InterruptedException ex) {
                        throw new RuntimeException("Thread interrupted during backoff ", ex);
                    }
                }
                statusMap.put(key, status);
            }
        }
        if (!sent) {
            throw new UnableToSendMailException("Sorry unable to send mail");
        }
        Utility.addToRateLimit(to,rateLimitMap);
        return status;
    }

    public String getStatus(String to, String subject, String body) {
        String key = Utility.makeKey(to, subject, body);
        if (statusMap.containsKey(key)) {
            return statusMap.get(key);
        }
        return "No status found for this email.";
    }

}
