package com.retrobased.market.repositories;

import com.retrobased.market.entities.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ProductTagRepository extends JpaRepository<ProductTag, UUID>, JpaSpecificationExecutor<ProductTag> {

}