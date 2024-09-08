package com.retrobased.market.repositories;

import com.retrobased.market.entities.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SellerRepository extends JpaRepository<Seller, String>, JpaSpecificationExecutor<Seller> {

}