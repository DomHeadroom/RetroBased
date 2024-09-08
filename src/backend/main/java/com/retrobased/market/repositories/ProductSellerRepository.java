package com.retrobased.market.repositories;

import com.retrobased.market.entities.ProductSeller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductSellerRepository extends JpaRepository<ProductSeller, String>, JpaSpecificationExecutor<ProductSeller> {

}