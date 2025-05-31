package com.ecommerce.shop.product.service.impl;

import com.ecommerce.shop.product.entity.Product;
import com.ecommerce.shop.product.repository.ProductRepository;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindProductById_success() throws BadRequestException {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.findProductById(1L);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
    }

    @Test
    void testFindProductById_notFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> productService.findProductById(1L)
        );

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void testFindByIdForUpdate_success() {
        Product product = new Product();
        product.setId(2L);

        when(productRepository.findByIdForUpdate(2L)).thenReturn(product);

        Product result = productService.findByIdForUpdate(2L);

        assertNotNull(result);
        assertEquals(2L, result.getId());
    }

    @Test
    void testUpdateProduct_success() {
        Product product = new Product();
        product.setId(3L);
        product.setName("Updated Product");

        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.updateProduct(product);

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
    }


}