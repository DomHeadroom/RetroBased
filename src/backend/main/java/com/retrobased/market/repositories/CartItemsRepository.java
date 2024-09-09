package com.retrobased.market.repositories;

import com.retrobased.market.entities.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface CartItemsRepository extends JpaRepository<CartItems, UUID>, JpaSpecificationExecutor<CartItems> {

    boolean existsByCartIdAndProductId(UUID cartId, UUID productId);
}