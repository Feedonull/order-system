package com.ecommerce.shop.payment.service.impl;

import com.ecommerce.shop.order.entity.Order;
import com.ecommerce.shop.order.enums.OrderStatus;
import com.ecommerce.shop.payment.client.PaymentClient;
import com.ecommerce.shop.payment.entity.Payment;
import com.ecommerce.shop.payment.repository.PaymentRepository;
import com.ecommerce.shop.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentClient paymentClient;

    @Override
    public boolean processPayment(Double amount, String paymentMethod, Order order) {
        if(paymentClient.process(amount, paymentMethod)){
            Payment payment = new Payment();
            payment.setAmount(amount);
            payment.setStatus(OrderStatus.COMPLETE.name());
            payment.setPaymentMethod(paymentMethod);
            payment.setOrder(order);
            paymentRepository.save(payment);
            return true;
        }
        return false;
    }
}
