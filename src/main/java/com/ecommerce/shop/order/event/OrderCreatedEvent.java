package com.ecommerce.shop.order.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderCreatedEvent {

    private Long orderId;
    private String customerEmail;

}
