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
import java.util.Set;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    @Query("SELECT p FROM Product p WHERE (p.productName LIKE :name OR p.productDescription LIKE :name) AND p.deleted = FALSE AND p.published = TRUE")
    Page<Product> find(@Param("name") String name, Pageable paging);

    @Query("SELECT p FROM Product p WHERE (LOWER(p.productName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(p.productDescription) LIKE LOWER(CONCAT('%', :name, '%'))) AND p.deleted = FALSE AND p.published = TRUE")
    Page<Product> findByNameIgnoreCase(@Param("name") String name, Pageable paging);

    Long findQuantityById(UUID id);

    @Query("SELECT p FROM Product p WHERE (p.id IN :productIds) AND p.deleted = FALSE")
    List<Product> findByIdIn(Set<UUID> productIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id IN :productIds")
    List<Product> findByIdInWithLock(Set<UUID> productIds);

    @Query(value = "SELECT * FROM products ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Product> findRandomProducts(@Param("limit") int limit);

    boolean existsByIdAndDeleted(UUID id, boolean deleted);

    boolean existsByIdAndDisableOutOfStock(UUID id, boolean disableOutOfStock);

    boolean existsByIdAndPublished(UUID id, boolean published);
}