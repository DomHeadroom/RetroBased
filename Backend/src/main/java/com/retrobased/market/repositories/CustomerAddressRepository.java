package com.retrobased.market.repositories;

import com.retrobased.market.entities.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, UUID>, JpaSpecificationExecutor<CustomerAddress> {

    Optional<CustomerAddress> findByIdAndCustomerIdAndDeletedFalse(UUID addressId, UUID customerId);

    List<CustomerAddress> findByCustomerIdAndDeletedFalse(UUID customerId);
}