package com.ecommerce.shop.payment.service;

import com.ecommerce.shop.order.entity.Order;

public interface PaymentService {

    boolean processPayment(Double amount, String paymentMethod, Order order);

}
