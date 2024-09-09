package com.retrobased.market.repositories;

import com.retrobased.market.entities.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface TagsRepository extends JpaRepository<Tags, UUID>, JpaSpecificationExecutor<Tags> {

}