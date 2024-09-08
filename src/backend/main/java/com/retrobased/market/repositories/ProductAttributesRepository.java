package com.retrobased.market.repositories;

import com.retrobased.market.entities.ProductAttributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductAttributesRepository extends JpaRepository<ProductAttributes, String>, JpaSpecificationExecutor<ProductAttributes> {

}