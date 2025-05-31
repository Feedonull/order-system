package com.ecommerce.shop.notification.email.service.impl;

import com.ecommerce.shop.notification.email.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SmtpEmailServiceTest {

    private EmailService emailService;

    @BeforeEach
    void setUp() {
        emailService = new SmtpEmailService();
    }

    @Test
    void testSendEmail_shouldLogAndDelay() {
        String to = "test@example.com";
        String subject = "Order Confirmation";
        String body = "Order #123 confirmed";

        long start = System.currentTimeMillis();
        emailService.sendEmail(to, subject, body);
        long end = System.currentTimeMillis();

        // Check if delay was approximately 6 seconds
        long duration = end - start;
        assertTrue(duration >= 5800, "Email sending should simulate delay");
    }

}