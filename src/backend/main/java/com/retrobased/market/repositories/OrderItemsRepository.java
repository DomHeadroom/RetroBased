package com.retrobased.market.repositories;

import com.retrobased.market.entities.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface OrderItemsRepository extends JpaRepository<OrderItems, UUID>, JpaSpecificationExecutor<OrderItems> {

}