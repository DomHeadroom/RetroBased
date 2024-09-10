package com.retrobased.market.repositories;

import com.retrobased.market.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE p.productName LIKE ?1 OR " +
            " p.productDescription LIKE ?1")
    Page<Product> find(String name, Pageable paging);

    Long findQuantityById(UUID id);
}