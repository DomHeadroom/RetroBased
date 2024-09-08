package com.retrobased.market.repositories;

import com.retrobased.market.entities.CustomerAddresses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerAddressesRepository extends JpaRepository<CustomerAddresses, String>, JpaSpecificationExecutor<CustomerAddresses> {

}