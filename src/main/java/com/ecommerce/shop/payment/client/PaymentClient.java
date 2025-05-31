package com.ecommerce.shop.payment.client;

public interface PaymentClient {

    boolean process(Double amount, String method);

}
