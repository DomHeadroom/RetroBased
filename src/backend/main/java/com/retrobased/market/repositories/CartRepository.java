package com.retrobased.market.repositories;

import com.retrobased.market.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CartRepository extends JpaRepository<Cart, String>, JpaSpecificationExecutor<Cart> {

}