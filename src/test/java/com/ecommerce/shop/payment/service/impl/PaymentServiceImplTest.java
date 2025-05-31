package com.ecommerce.shop.payment.service.impl;

import com.ecommerce.shop.order.entity.Order;
import com.ecommerce.shop.payment.client.PaymentClient;
import com.ecommerce.shop.payment.entity.Payment;
import com.ecommerce.shop.payment.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentClient paymentClient;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processPayment_shouldSavePayment_whenPaymentIsSuccessful() {
        Double amount = 150.0;
        String paymentMethod = "CARD";
        Order order = new Order();

        when(paymentClient.process(amount, paymentMethod)).thenReturn(true);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Boolean result = paymentService.processPayment(amount, paymentMethod, order);

        assertTrue(result);
        verify(paymentClient, times(1)).process(amount, paymentMethod);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void processPayment_shouldNotSavePayment_whenPaymentFails() {
        Double amount = 300.0;
        String paymentMethod = "PAYPAL";
        Order order = new Order();

        when(paymentClient.process(amount, paymentMethod)).thenReturn(false);

        Boolean result = paymentService.processPayment(amount, paymentMethod, order);

        assertFalse(result);
        verify(paymentClient, times(1)).process(amount, paymentMethod);
        verify(paymentRepository, never()).save(any(Payment.class));
    }

}