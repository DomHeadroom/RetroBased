package com.retrobased.market.repositories;

import com.retrobased.market.entities.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CartItemsRepository extends JpaRepository<CartItems, String>, JpaSpecificationExecutor<CartItems> {

}