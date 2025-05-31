package com.ecommerce.shop.customer.service;

import com.ecommerce.shop.customer.entity.Customer;
import org.apache.coyote.BadRequestException;

public interface CustomerService {

    Customer findCustomerById(Long id) throws BadRequestException;
    Customer saveCustomer(Customer customer);

}
