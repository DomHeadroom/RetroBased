package com.retrobased.market.repositories;

import com.retrobased.market.entities.Slideshow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SlideshowRepository extends JpaRepository<Slideshow, UUID>, JpaSpecificationExecutor<Slideshow> {

}