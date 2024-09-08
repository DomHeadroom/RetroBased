package com.retrobased.market.repositories;

import com.retrobased.market.entities.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GalleryRepository extends JpaRepository<Gallery, String>, JpaSpecificationExecutor<Gallery> {

}