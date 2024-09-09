package com.retrobased.market.repositories;

import com.retrobased.market.entities.ProductCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ProductCategoriesRepository extends JpaRepository<ProductCategories, UUID>, JpaSpecificationExecutor<ProductCategories> {

}