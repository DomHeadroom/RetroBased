package com.retrobased.market.repositories;

import com.retrobased.market.entities.Attributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AttributesRepository extends JpaRepository<Attributes, String>, JpaSpecificationExecutor<Attributes> {

}