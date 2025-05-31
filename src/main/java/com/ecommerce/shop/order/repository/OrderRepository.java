package com.ecommerce.shop.order.repository;

import com.ecommerce.shop.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAll(Pageable pageable);
    @Query(value = "SELECT * FROM orders WHERE total_amount > 1000",
            countQuery = "SELECT count(*) FROM orders WHERE total_amount > 1000",
            nativeQuery = true)
    Page<Order> findHighValueOrders(Pageable pageable);

}
