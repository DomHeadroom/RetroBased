package com.retrobased.market.repositories;

import com.retrobased.market.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID>, JpaSpecificationExecutor<CartItem> {

    boolean existsByCartIdAndProductId(UUID cartId, UUID productId);

    Long findQuantityByCartIdAndProductId(UUID cartId, UUID productId);

    void deleteByCartIdAndProductId(UUID cartId, UUID productId);

    int updateQuantityByCartIdAndProductId(UUID cartId, UUID productId, Long quantity);
}