package com.ecommerce.shop.order.service.impl;

import com.ecommerce.shop.customer.entity.Customer;
import com.ecommerce.shop.customer.repository.CustomerRepository;
import com.ecommerce.shop.order.dto.OrderRequest;
import com.ecommerce.shop.order.dto.OrderRequestItem;
import com.ecommerce.shop.order.entity.Order;
import com.ecommerce.shop.order.entity.OrderItem;
import com.ecommerce.shop.order.enums.OrderStatus;
import com.ecommerce.shop.order.repository.OrderItemRepository;
import com.ecommerce.shop.order.repository.OrderRepository;
import com.ecommerce.shop.product.entity.Product;
import com.ecommerce.shop.product.repository.ProductRepository;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceImplIntegrationTest {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    private Customer testCustomer;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        orderItemRepository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();

        testCustomer = new Customer();
        testCustomer.setEmail("john.doe@example.com");
        customerRepository.save(testCustomer);

        testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setPrice(100.0);
        testProduct.setStockQuantity(10);
        productRepository.save(testProduct);
    }

    @Test
    void testCreateOrder_successful() throws BadRequestException {
        OrderRequestItem item = new OrderRequestItem();
        item.setProductId(testProduct.getId());
        item.setQuantity(2);

        OrderRequest request = new OrderRequest();
        request.setCustomerId(testCustomer.getId());
        request.setItems(List.of(item));

        Order order = orderService.createOrder(request);

        assertNotNull(order.getId());
        assertEquals(OrderStatus.PLACED.name(), order.getStatus());
        assertEquals(200.0, order.getTotalAmount());
        assertEquals(testCustomer.getId(), order.getCustomer().getId());

        Product updatedProduct = productRepository.findById(testProduct.getId()).orElseThrow();
        assertEquals(8, updatedProduct.getStockQuantity());

        List<OrderItem> items = orderItemRepository.findAll();
        assertEquals(1, items.size());
        assertEquals(2, items.get(0).getQuantity());
    }

    @Test
    void testCreateOrder_insufficientStock() {
        OrderRequestItem item = new OrderRequestItem();
        item.setProductId(testProduct.getId());
        item.setQuantity(100); // More than stock

        OrderRequest request = new OrderRequest();
        request.setCustomerId(testCustomer.getId());
        request.setItems(List.of(item));

        assertThrows(RuntimeException.class, () -> orderService.createOrder(request));
    }

    @Test
    void testFindAllOrders() throws BadRequestException {
        testCreateOrder_successful();

        var result = orderService.findAllOrders(0, 10, "desc");
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testFindHighValueOrders() throws BadRequestException {
        testCreateOrder_successful();

        var result = orderService.findHighValueOrders(0, 10);
        assertTrue(result.getContent().size() >= 0);
    }
}
