package com.retrobased.market.repositories;

import com.retrobased.market.entities.Customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface CustomersRepository extends JpaRepository<Customers, UUID>, JpaSpecificationExecutor<Customers> {

    boolean existsByEmail(String email);
}