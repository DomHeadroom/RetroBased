package com.retrobased.market.repositories;

import com.retrobased.market.entities.Slideshows;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SlideshowsRepository extends JpaRepository<Slideshows, UUID>, JpaSpecificationExecutor<Slideshows> {

}