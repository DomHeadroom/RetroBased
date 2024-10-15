package com.retrobased.market.repositories;

import com.retrobased.market.entities.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    Page<Product> findByProductNameContainingIgnoreCaseOrProductDescriptionContainingIgnoreCase(String productName, String productDescription, Pageable paging);

    Long findQuantityById(UUID id);

    // TODO riscrivere ste robe con metodi autogenerati da spring
    @Query("SELECT p FROM Product p WHERE (p.id IN :productIds) AND p.deleted = FALSE")
    List<Product> findByIdIn(Set<UUID> productIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id IN :productIds")
    List<Product> findByIdInWithLock(Set<UUID> productIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :productId AND p.deleted = false AND p.published = true")
    Optional<Product> findByIdWithLock(UUID productId);

    @Query(value = "SELECT * FROM products ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Product> findRandomProducts(@Param("limit") int limit);

    boolean existsByIdAndDeleted(UUID id, boolean deleted);

    boolean existsByIdAndDisableOutOfStock(UUID id, boolean disableOutOfStock);

    boolean existsByIdAndPublished(UUID id, boolean published);

    Optional<Product> findByIdAndDeletedIsFalseAndPublishedIsTrue(UUID id);

    Optional<Product> findBySlug(String slug);
}