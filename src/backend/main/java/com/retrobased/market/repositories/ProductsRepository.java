package com.retrobased.market.repositories;

import com.retrobased.market.entities.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductsRepository extends JpaRepository<Products, UUID>, JpaSpecificationExecutor<Products> {

    @Query("SELECT p " +
            "FROM Products p " +
            "WHERE p.productName LIKE ?1 OR " +
            " p.productDescription LIKE ?1")
    Page<Products> find(String name, Pageable paging);

    Long findQuantityById(UUID id);
}