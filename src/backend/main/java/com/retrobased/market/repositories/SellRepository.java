package com.retrobased.market.repositories;

import com.retrobased.market.entities.Sell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SellRepository extends JpaRepository<Sell, Long>, JpaSpecificationExecutor<Sell> {

}