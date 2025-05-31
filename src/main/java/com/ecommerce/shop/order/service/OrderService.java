package com.ecommerce.shop.order.service;

import com.ecommerce.shop.order.dto.OrderRequest;
import com.ecommerce.shop.order.entity.Order;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;

public interface OrderService {

    Order createOrder(OrderRequest orderRequest) throws BadRequestException;
    Page<Order> findAllOrders(Integer page, Integer size, String sortDir);
    Page<Order> findHighValueOrders(Integer page, Integer size);

}
