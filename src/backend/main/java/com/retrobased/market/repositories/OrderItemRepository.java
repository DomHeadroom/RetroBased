package com.retrobased.market.repositories;

import com.retrobased.market.entities.Order;
import com.retrobased.market.entities.OrderItem;
import com.retrobased.market.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID>, JpaSpecificationExecutor<OrderItem> {


    Page<Product> findByOrderId(UUID orderId, Pageable paging);
}