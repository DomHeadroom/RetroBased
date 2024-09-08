package com.retrobased.market.repositories;

import com.retrobased.market.entities.Slideshows;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SlideshowsRepository extends JpaRepository<Slideshows, String>, JpaSpecificationExecutor<Slideshows> {

}