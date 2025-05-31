package com.ecommerce.shop.product.service;

import com.ecommerce.shop.product.entity.Product;
import org.apache.coyote.BadRequestException;

public interface ProductService {

    Product findProductById(Long id) throws BadRequestException;

    Product findByIdForUpdate(Long id);
    Product updateProduct(Product product);

}
