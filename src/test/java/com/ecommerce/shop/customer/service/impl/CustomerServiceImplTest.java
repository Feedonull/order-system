package com.ecommerce.shop.customer.service.impl;

import com.ecommerce.shop.customer.entity.Customer;
import com.ecommerce.shop.customer.repository.CustomerRepository;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    private CustomerRepository customerRepository;
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        customerRepository = Mockito.mock(CustomerRepository.class);
        customerService = new CustomerServiceImpl(customerRepository);
    }

    @Test
    void testFindCustomerById_Success() throws BadRequestException {

        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);
        mockCustomer.setEmail("JohnDoe@gmail.com");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));


        Customer result = customerService.findCustomerById(1L);


        assertNotNull(result);
        assertEquals("JohnDoe@gmail.com", result.getEmail());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testFindCustomerById_NotFound() {
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());

        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            customerService.findCustomerById(2L);
        });

        assertEquals("User not found", ex.getMessage());
        verify(customerRepository, times(1)).findById(2L);
    }

}