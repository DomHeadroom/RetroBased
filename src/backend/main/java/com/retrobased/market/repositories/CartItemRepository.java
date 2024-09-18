package com.retrobased.market.repositories;

import com.retrobased.market.entities.CartItem;
import com.retrobased.market.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID>, JpaSpecificationExecutor<CartItem> {

    @Modifying
    @Transactional
    @Query("UPDATE CartItem c SET c.quantity = :quantity WHERE c.cart.id = :cartId AND c.product.id = :productId")
    int updateQuantityByCartIdAndProductId(@Param("cartId") UUID cartId, @Param("productId") UUID productId, @Param("quantity") Long quantity);

    boolean existsByCartIdAndProductId(UUID cartId, UUID productId);

    Long findQuantityByCartIdAndProductId(UUID cartId, UUID productId);

    void deleteByCartIdAndProductId(UUID cartId, UUID productId);

    @Query("SELECT c.product FROM CartItem c WHERE c.cart.id = :cartId")
    Page<Product> findProductsByCartId(@Param("cartId") UUID cartId, Pageable pageable);

    @Query("SELECT c.quantity FROM CartItem c WHERE c.cart.id = :cartId AND c.product.id = :productId")
    Long getQuantityByCartIdAndProductId(@Param("cartId") UUID cartId,@Param("productId") UUID productId);

    Optional<CartItem> findByCartIdAndProductId(UUID id, UUID id1);
}