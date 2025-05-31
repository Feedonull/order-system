package com.ecommerce.shop.notification.email.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailServiceFactory {

    private final Map<String, EmailService> serviceMap;

    public EmailServiceFactory(List<EmailService> services) {
        serviceMap = new HashMap<>();
        for (EmailService service : services) {
            String key = service.getClass().getAnnotation(Service.class).value();
            serviceMap.put(key, service);
        }
    }

    public EmailService getEmailService(String provider) {
        return serviceMap.getOrDefault(provider, serviceMap.get("smtpEmailService"));
    }

}
