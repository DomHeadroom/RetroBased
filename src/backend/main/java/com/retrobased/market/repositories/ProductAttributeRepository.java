package com.retrobased.market.repositories;

import com.retrobased.market.entities.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, UUID>, JpaSpecificationExecutor<ProductAttribute> {

}