package com.ecommerce.shop.payment.entity;

import com.ecommerce.shop.order.entity.Order;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "payments")
@Data
@RequiredArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Double amount;
    private String Status;
    private String paymentMethod;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id,", referencedColumnName = "id")
    private Order order;

}
