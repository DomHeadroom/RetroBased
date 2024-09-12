package com.retrobased.market.repositories;

import com.retrobased.market.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID>, JpaSpecificationExecutor<CartItem> {

    @Modifying
    @Transactional
    @Query("UPDATE CartItem ci SET ci.quantity = :quantity WHERE ci.cart.id = :cartId AND ci.product.id = :productId")
    int updateQuantityByCartIdAndProductId(@Param("cartId") UUID cartId, @Param("productId") UUID productId, @Param("quantity") Long quantity);

    boolean existsByCartIdAndProductId(UUID cartId, UUID productId);

    Long findQuantityByCartIdAndProductId(UUID cartId, UUID productId);

    void deleteByCartIdAndProductId(UUID cartId, UUID productId);

}