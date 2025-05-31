package com.ecommerce.shop.notification.email.service;

public interface EmailService {

    void sendEmail(String to, String subject, String body);

}
