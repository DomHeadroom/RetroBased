package com.retrobased.market.repositories;

import com.retrobased.market.entities.OrderItem;
import com.retrobased.market.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID>, JpaSpecificationExecutor<OrderItem> {

    @Query("SELECT oi.product FROM OrderItem oi WHERE oi.order.id = :orderId")
    Page<Product> findByOrderId(@Param("orderId") UUID orderId, Pageable paging);

    Page<OrderItem> getByOrderId(UUID orderId, Pageable paging);
}