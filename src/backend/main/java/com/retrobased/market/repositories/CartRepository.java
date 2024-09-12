package com.retrobased.market.repositories;

import com.retrobased.market.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID>, JpaSpecificationExecutor<Cart> {
    boolean existsByCustomerId(UUID customerId);

    Cart getCartByCustomerId(UUID customerId);
}