package com.retrobased.market.services;

import com.retrobased.market.entities.Attribute;
import com.retrobased.market.repositories.AttributeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AttributeService {

    private final AttributeRepository attributeRepository;

    public AttributeService(AttributeRepository attributeRepository) {
        this.attributeRepository = attributeRepository;
    }

    @Transactional(readOnly = true)
    public boolean exists(UUID id) {
        return attributeRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public Attribute get(UUID id) {
        return attributeRepository.getReferenceById(id);
    }
}
