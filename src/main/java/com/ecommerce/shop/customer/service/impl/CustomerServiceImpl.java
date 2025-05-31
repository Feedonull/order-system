package com.ecommerce.shop.customer.service.impl;

import com.ecommerce.shop.customer.entity.Customer;
import com.ecommerce.shop.customer.repository.CustomerRepository;
import com.ecommerce.shop.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    @Override
    public Customer findCustomerById(Long id) throws BadRequestException {
        return customerRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("User not found"));
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
}
