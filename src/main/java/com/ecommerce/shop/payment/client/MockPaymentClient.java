package com.ecommerce.shop.payment.client;

import org.springframework.stereotype.Component;

@Component
public class MockPaymentClient implements PaymentClient{
    @Override
    public boolean process(Double amount, String method) {
        return true;
    }
}
