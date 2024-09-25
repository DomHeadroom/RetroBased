package com.retrobased.market.repositories;

import com.retrobased.market.entities.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID>, JpaSpecificationExecutor<CartItem> {

    Page<CartItem> findByCartId(UUID cartId, Pageable pageable);

    Optional<CartItem> findByCartIdAndProductId(UUID cartId, UUID productId);
}