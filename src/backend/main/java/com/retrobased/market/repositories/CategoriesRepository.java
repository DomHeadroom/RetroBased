package com.retrobased.market.repositories;

import com.retrobased.market.entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface CategoriesRepository extends JpaRepository<Categories, UUID>, JpaSpecificationExecutor<Categories> {

}