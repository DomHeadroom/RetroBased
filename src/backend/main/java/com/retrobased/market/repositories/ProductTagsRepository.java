package com.retrobased.market.repositories;

import com.retrobased.market.entities.ProductTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductTagsRepository extends JpaRepository<ProductTags, String>, JpaSpecificationExecutor<ProductTags> {

}