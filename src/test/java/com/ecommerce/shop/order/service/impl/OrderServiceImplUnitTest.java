package com.ecommerce.shop.order.service.impl;

import com.ecommerce.shop.customer.entity.Customer;
import com.ecommerce.shop.customer.service.CustomerService;
import com.ecommerce.shop.exception.InsufficientStockException;
import com.ecommerce.shop.exception.PaymentFailedException;
import com.ecommerce.shop.order.dto.OrderRequest;
import com.ecommerce.shop.order.dto.OrderRequestItem;
import com.ecommerce.shop.order.entity.Order;
import com.ecommerce.shop.order.event.OrderCreatedEvent;
import com.ecommerce.shop.order.repository.OrderItemRepository;
import com.ecommerce.shop.order.repository.OrderRepository;
import com.ecommerce.shop.payment.service.PaymentService;
import com.ecommerce.shop.product.entity.Product;
import com.ecommerce.shop.product.service.ProductService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplUnitTest {

    @Mock private CustomerService customerService;
    @Mock private ProductService productService;
    @Mock private OrderRepository orderRepository;
    @Mock private OrderItemRepository orderItemRepository;
    @Mock private PaymentService paymentService;
    @Mock private ApplicationEventPublisher eventPublisher;

    @InjectMocks private OrderServiceImpl orderService;

    private OrderRequest orderRequest;
    private Customer mockCustomer;
    private Product mockProduct;
    private Order mockOrder;

    @BeforeEach
    void setUp() {
        mockCustomer = new Customer();
        mockCustomer.setId(1L);

        mockProduct = new Product();
        mockProduct.setId(101L);
        mockProduct.setName("Laptop");
        mockProduct.setPrice(1000.0);
        mockProduct.setStockQuantity(10);

        OrderRequestItem item = new OrderRequestItem();
        item.setProductId(101L);
        item.setQuantity(2);

        orderRequest = new OrderRequest();
        orderRequest.setCustomerId(1L);
        orderRequest.setItems(List.of(item));

        mockOrder = new Order();
        mockOrder.setId(1L);
        mockOrder.setTotalAmount(1500.0);
        mockOrder.setStatus("PLACED");
        mockOrder.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createOrder_Success() throws BadRequestException {
        when(customerService.findCustomerById(1L)).thenReturn(mockCustomer);
        when(productService.findByIdForUpdate(101L)).thenReturn(mockProduct);
        when(paymentService.processPayment(anyDouble(), anyString(), any(Order.class))).thenReturn(true);

        Order savedOrder = new Order();
        savedOrder.setId(99L);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        Order order = orderService.createOrder(orderRequest);

        assertNotNull(order);
        verify(orderRepository).save(any(Order.class));
        verify(orderItemRepository).saveAll(anyList());
        verify(productService).updateProduct(any(Product.class));
        verify(eventPublisher).publishEvent(any(OrderCreatedEvent.class));
    }

    @Test
    void createOrder_ThrowsInsufficientStockException() throws BadRequestException {
        mockProduct.setStockQuantity(1); // less than requested

        when(customerService.findCustomerById(1L)).thenReturn(mockCustomer);
        when(productService.findByIdForUpdate(101L)).thenReturn(mockProduct);

        assertThrows(InsufficientStockException.class, () -> orderService.createOrder(orderRequest));

        verify(productService, never()).updateProduct(any());
        verify(orderRepository, never()).save(any());
        verify(paymentService, never()).processPayment(anyDouble(), anyString(), any());
    }

    @Test
    void createOrder_ThrowsPaymentFailedException() throws BadRequestException {
        when(customerService.findCustomerById(1L)).thenReturn(mockCustomer);
        when(productService.findByIdForUpdate(101L)).thenReturn(mockProduct);
        when(paymentService.processPayment(anyDouble(), anyString(), any())).thenReturn(false);

        assertThrows(PaymentFailedException.class, () -> orderService.createOrder(orderRequest));

        verify(orderRepository).save(any());
        verify(orderItemRepository).saveAll(any());
        verify(eventPublisher, never()).publishEvent(any());
    }


    @Test
    void testFindAllOrders() {
        List<Order> orderList = List.of(mockOrder);
        Page<Order> page = new PageImpl<>(orderList);
        Pageable expectedPageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        when(orderRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Order> result = orderService.findAllOrders(0, 10, "desc");

        assertEquals(1, result.getContent().size());
        assertEquals(mockOrder.getId(), result.getContent().get(0).getId());
        verify(orderRepository).findAll(expectedPageable);
    }

    @Test
    void testFindHighValueOrders() {
        List<Order> highValueOrders = List.of(mockOrder);
        Page<Order> page = new PageImpl<>(highValueOrders);
        Pageable expectedPageable = PageRequest.of(0, 5);

        when(orderRepository.findHighValueOrders(any(Pageable.class))).thenReturn(page);

        Page<Order> result = orderService.findHighValueOrders(0, 5);

        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().get(0).getTotalAmount() > 1000);
        verify(orderRepository).findHighValueOrders(expectedPageable);
    }

}