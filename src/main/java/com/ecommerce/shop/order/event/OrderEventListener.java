package com.ecommerce.shop.order.event;

import com.ecommerce.shop.notification.email.service.EmailServiceFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    @Value("${email.provider}")
    private String emailProvider;
    private final EmailServiceFactory emailServiceFactory;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleOrderCreated(OrderCreatedEvent event) {
        // Simulate sending email and logging
        try {
            // Simulate delay
            Thread.sleep(1000);
            log.info("[ASYNC LOG] Order created with ID: " + event.getOrderId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        emailServiceFactory.getEmailService(emailProvider)
                .sendEmail(event.getCustomerEmail(), "Order Confirmed", "Your order is placed.\nOrder id:"+event.getOrderId());
    }

}
