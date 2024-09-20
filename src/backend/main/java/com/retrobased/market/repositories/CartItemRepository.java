package com.retrobased.market.repositories;

import com.retrobased.market.entities.CartItem;
import com.retrobased.market.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID>, JpaSpecificationExecutor<CartItem> {

    boolean existsByCartIdAndProductId(UUID cartId, UUID productId);

    Long findQuantityByCartIdAndProductId(UUID cartId, UUID productId);

    @Query("SELECT c.product FROM CartItem c WHERE c.cart.id = :cartId")
    Page<Product> findProductsByCartId(@Param("cartId") UUID cartId, Pageable pageable);

    Optional<CartItem> findByCartIdAndProductId(UUID id, UUID id1);
}