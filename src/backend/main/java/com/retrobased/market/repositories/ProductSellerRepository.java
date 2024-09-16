package com.retrobased.market.repositories;

import com.retrobased.market.entities.ProductSeller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ProductSellerRepository extends JpaRepository<ProductSeller, UUID>, JpaSpecificationExecutor<ProductSeller> {

    @Transactional
    void deleteById(ProductSeller.ProductSellerId id);
}