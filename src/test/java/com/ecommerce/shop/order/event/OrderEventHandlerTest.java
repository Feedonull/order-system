package com.ecommerce.shop.order.event;

import com.ecommerce.shop.notification.email.service.EmailService;
import com.ecommerce.shop.notification.email.service.EmailServiceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

class OrderEventHandlerTest {

    private EmailServiceFactory emailServiceFactory;
    private EmailService emailService;
    private OrderEventHandler orderEventHandler;

    @BeforeEach
    void setup() {
        emailServiceFactory = Mockito.mock(EmailServiceFactory.class);
        emailService = Mockito.mock(EmailService.class);

        Mockito.when(emailServiceFactory.getEmailService(anyString())).thenReturn(emailService);

        orderEventHandler = new OrderEventHandler(emailServiceFactory);

        ReflectionTestUtils.setField(orderEventHandler, "emailProvider", "smtp");
    }

    @Test
    void testOrderCreatedEvent_SendsEmail() {
        OrderCreatedEvent event = new OrderCreatedEvent(100L, "test@example.com");

        orderEventHandler.orderCreatedEvent(event);


        verify(emailService, timeout(100000)).sendEmail(
                eq("test@example.com"),
                eq("Order Confirmed"),
                contains("100")
        );
    }

}