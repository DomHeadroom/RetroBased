package com.retrobased.market.repositories;

import com.retrobased.market.entities.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface AttributeRepository extends JpaRepository<Attribute, UUID>, JpaSpecificationExecutor<Attribute> {

}