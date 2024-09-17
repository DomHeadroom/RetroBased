package com.retrobased.market.services;

import com.retrobased.market.entities.Attribute;
import com.retrobased.market.repositories.AttributeRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AttributeService {

    private final AttributeRepository attributeRepository;

    public AttributeService(AttributeRepository attributeRepository) {
        this.attributeRepository = attributeRepository;
    }

    public boolean existsById(UUID categoryId) {
        return attributeRepository.existsById(categoryId);
    }

    public Attribute getById(UUID categoryId) {
        return attributeRepository.getReferenceById(categoryId);
    }
}
