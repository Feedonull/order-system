package com.ecommerce.shop.order.service.impl;

import com.ecommerce.shop.customer.entity.Customer;
import com.ecommerce.shop.customer.service.CustomerService;
import com.ecommerce.shop.exception.InsufficientStockException;
import com.ecommerce.shop.exception.PaymentFailedException;
import com.ecommerce.shop.order.dto.OrderRequest;
import com.ecommerce.shop.order.dto.OrderRequestItem;
import com.ecommerce.shop.order.entity.Order;
import com.ecommerce.shop.order.entity.OrderItem;
import com.ecommerce.shop.order.enums.OrderStatus;
import com.ecommerce.shop.order.event.OrderCreatedEvent;
import com.ecommerce.shop.order.repository.OrderItemRepository;
import com.ecommerce.shop.order.repository.OrderRepository;
import com.ecommerce.shop.order.service.OrderService;
import com.ecommerce.shop.payment.enums.PaymentMethod;
import com.ecommerce.shop.payment.service.PaymentService;
import com.ecommerce.shop.product.entity.Product;
import com.ecommerce.shop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CustomerService customerService;
    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentService paymentService;
    private final ApplicationEventPublisher eventPublisher;
    @Override
    @Transactional
    public Order createOrder(OrderRequest orderRequest) throws BadRequestException {

        // Validate customer
        Customer customer = customerService.findCustomerById(orderRequest.getCustomerId());

        Order order = new Order();
        order.setCustomer(customer);

        List<OrderItem> orderItems = new ArrayList<>();
        Double totalAmount = 0.0;
        // items
        for(OrderRequestItem item : orderRequest.getItems()){
            // validate product and stock
            Product product = productService.findByIdForUpdate(item.getProductId());
            if(product.getStockQuantity() < item.getQuantity()){
                throw new InsufficientStockException("Product:" + product.getName());
            }
            // update product stock
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productService.updateProduct(product);
            // add item to order
            Double itemPrice = product.getPrice() * item.getQuantity();
            orderItems.add(new OrderItem(order, product, item.getQuantity(), itemPrice));
            // update total amount
            totalAmount += itemPrice;
        }
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PLACED.name());
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);
        if(!paymentService.processPayment(totalAmount, PaymentMethod.CASH_ON_DELIVERY.name(),order)){
            throw new PaymentFailedException("Payment failed");
        }
        eventPublisher.publishEvent(new OrderCreatedEvent(order.getId(), customer.getEmail()));
        return order;
    }

    @Override
    public Page<Order> findAllOrders(Integer page, Integer size, String sortDir) {
        Sort sort = Sort.by("createdAt");
        sort = "desc".equalsIgnoreCase(sortDir) ? sort.descending() : sort.ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return orderRepository.findAll(pageable);
    }

    @Override
    public Page<Order> findHighValueOrders(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findHighValueOrders(pageable);
    }
}
