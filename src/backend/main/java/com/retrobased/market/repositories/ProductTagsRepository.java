package com.retrobased.market.repositories;

import com.retrobased.market.entities.ProductTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ProductTagsRepository extends JpaRepository<ProductTags, UUID>, JpaSpecificationExecutor<ProductTags> {

}