package com.retrobased.market.repositories;

import com.retrobased.market.entities.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    @Query("SELECT p FROM Product p WHERE (p.productName LIKE :name OR p.productDescription LIKE :name) AND p.deleted = FALSE")
    Page<Product> find(@Param("name") String name, Pageable paging);

    @Query("SELECT p FROM Product p WHERE (LOWER(p.productName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(p.productDescription) LIKE LOWER(CONCAT('%', :name, '%'))) AND p.deleted = FALSE")
    Page<Product> findByNameIgnoreCase(@Param("name") String name, Pageable paging);

    Long findQuantityById(UUID id);

    @Query("SELECT p FROM Product p WHERE (p.id IN :productIds) AND p.deleted = FALSE")
    List<Product> findByIdIn(List<UUID> productIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE (p.id IN :productIds) AND p.deleted = FALSE")
    List<Product> findByIdInWithLock(List<UUID> productIds);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.deleted = TRUE WHERE p.id = :productId")
    int removeProduct(UUID productId);
}