package com.ecommerce.shop.order.event;

import com.ecommerce.shop.customer.entity.Customer;
import com.ecommerce.shop.customer.service.CustomerService;
import com.ecommerce.shop.order.dto.OrderRequest;
import com.ecommerce.shop.order.dto.OrderRequestItem;
import com.ecommerce.shop.order.repository.OrderRepository;
import com.ecommerce.shop.order.service.OrderService;
import com.ecommerce.shop.product.entity.Product;
import com.ecommerce.shop.product.service.ProductService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class OrderEventListenerIntegrationTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderRepository orderRepository;
    @SpyBean
    private OrderEventListener orderEventListener;

    private Customer testCustomer;
    private Product testProduct;


    @BeforeEach
    void setup(){
        orderRepository.deleteAll();

        testCustomer = new Customer();
        testCustomer.setEmail("john.doe@example.com");
        customerService.saveCustomer(testCustomer);

        testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setPrice(100.0);
        testProduct.setStockQuantity(10);
        productService.updateProduct(testProduct);
    }

    @Test
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void testOrderCreatedEventIsHandledBeforeCommit() throws BadRequestException {

        OrderRequestItem item = new OrderRequestItem();
        item.setProductId(testProduct.getId());
        item.setQuantity(2);

        OrderRequest request = new OrderRequest();
        request.setCustomerId(testCustomer.getId());
        request.setItems(List.of(item));

        orderService.createOrder(request);


        verify(orderEventListener, times(1))
                .handleOrderCreated(any(OrderCreatedEvent.class));

        assertEquals(1, orderRepository.findAll().size());
    }

}
