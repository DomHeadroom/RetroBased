package com.retrobased.market.repositories;

import com.retrobased.market.entities.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductsRepository extends JpaRepository<Products, String>, JpaSpecificationExecutor<Products> {

}