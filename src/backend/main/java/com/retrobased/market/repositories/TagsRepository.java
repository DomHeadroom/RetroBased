package com.retrobased.market.repositories;

import com.retrobased.market.entities.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TagsRepository extends JpaRepository<Tags, String>, JpaSpecificationExecutor<Tags> {

}