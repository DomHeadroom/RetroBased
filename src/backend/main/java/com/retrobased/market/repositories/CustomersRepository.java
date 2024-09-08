package com.retrobased.market.repositories;

import com.retrobased.market.entities.Customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomersRepository extends JpaRepository<Customers, String>, JpaSpecificationExecutor<Customers> {

    boolean existsByEmail(String email);
}