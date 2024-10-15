package com.retrobased.market.repositories;

import com.retrobased.market.entities.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    Page<Product> findByProductNameContainingIgnoreCaseOrProductDescriptionContainingIgnoreCase(String productName, String productDescription, Pageable paging);

    Long findQuantityById(UUID id);

    List<Product> findByIdInAndDeletedFalse(Set<UUID> productIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Product> findByIdIn(Set<UUID> productIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Product> findByIdAndDeletedFalseAndPublishedTrue(UUID productId);

    @Query(value = "SELECT * FROM products ORDER BY RANDOM()", nativeQuery = true)
    List<Product> findRandomProducts(Pageable paging);

    boolean existsByIdAndDeleted(UUID id, boolean deleted);

    boolean existsByIdAndDisableOutOfStock(UUID id, boolean disableOutOfStock);

    boolean existsByIdAndPublished(UUID id, boolean published);

    Optional<Product> findByIdAndDeletedIsFalseAndPublishedIsTrue(UUID id);

    Optional<Product> findBySlug(String slug);
}