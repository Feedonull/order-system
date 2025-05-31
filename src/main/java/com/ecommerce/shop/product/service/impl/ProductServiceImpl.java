package com.ecommerce.shop.product.service.impl;

import com.ecommerce.shop.product.entity.Product;
import com.ecommerce.shop.product.repository.ProductRepository;
import com.ecommerce.shop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product findProductById(Long id) throws BadRequestException {
        return productRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Product not found"));
    }

    @Override
    public Product findByIdForUpdate(Long id) {
        return productRepository.findByIdForUpdate(id);
    }

    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }
}
