package com.retrobased.market.repositories;

import com.retrobased.market.entities.Sells;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SellsRepository extends JpaRepository<Sells, Long>, JpaSpecificationExecutor<Sells> {

}