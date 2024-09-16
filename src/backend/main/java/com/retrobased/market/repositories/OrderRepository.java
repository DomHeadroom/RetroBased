package com.retrobased.market.repositories;

import com.retrobased.market.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {

    Page<Order> findByCustomerId(UUID customerId, Pageable paging);

    boolean existsByCustomerIdAndId(UUID customerId, UUID id);

    boolean existsOrderForCustomer(UUID productId, UUID sellerId);
}