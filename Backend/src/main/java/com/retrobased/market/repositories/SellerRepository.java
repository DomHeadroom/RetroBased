package com.retrobased.market.repositories;

import com.retrobased.market.entities.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SellerRepository extends JpaRepository<Seller, UUID>, JpaSpecificationExecutor<Seller> {

}