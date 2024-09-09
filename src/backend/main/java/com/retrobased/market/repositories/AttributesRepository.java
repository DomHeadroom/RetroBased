package com.retrobased.market.repositories;

import com.retrobased.market.entities.Attributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface AttributesRepository extends JpaRepository<Attributes, UUID>, JpaSpecificationExecutor<Attributes> {

}