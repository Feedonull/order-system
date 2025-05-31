package com.ecommerce.shop.notification.email.service.impl;

import com.ecommerce.shop.notification.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("smtpEmailService")
@RequiredArgsConstructor
@Slf4j
public class SmtpEmailService implements EmailService {
    @Override
    public void sendEmail(String to, String subject, String body) {

        try {
            // Simulate delay
            Thread.sleep(6000);
            log.info("Sending confirmation for order {}", body);
            log.info("Email sent:\n" +
                    "to: "+to+"\n" +
                    "Subject: "+subject+"\n" +
                    "Body:"+body);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
}
