package com.retrobased.market.repositories;

import com.retrobased.market.entities.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface GalleryRepository extends JpaRepository<Gallery, UUID>, JpaSpecificationExecutor<Gallery> {

}