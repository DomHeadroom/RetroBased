package com.retrobased.market.repositories;

import com.retrobased.market.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    @Query("SELECT p FROM Product p WHERE p.productName LIKE :name OR p.productDescription LIKE :name")
    Page<Product> find(@Param("name") String name, Pageable paging);

    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(p.productDescription) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Product> findByNameIgnoreCase(@Param("name") String name, Pageable paging);

    Long findQuantityById(UUID id);
}