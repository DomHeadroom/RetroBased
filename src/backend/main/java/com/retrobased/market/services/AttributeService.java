package com.retrobased.market.services;

import com.retrobased.market.entities.Attribute;
import com.retrobased.market.repositories.AttributeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class AttributeService {

    private final AttributeRepository attributeRepository;

    public AttributeService(AttributeRepository attributeRepository) {
        this.attributeRepository = attributeRepository;
    }

    /**
     * Checks if an attribute with the given ID exists.
     *
     * @param id the ID of the attribute
     * @return true if the attribute exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean exists(UUID id) {
        return attributeRepository.existsById(id);
    }

    /**
     * Retrieves an attribute by its ID.
     *
     * @param id the ID of the attribute
     * @return an Optional containing the attribute if found, empty otherwise
     */
    @Transactional(readOnly = true)
    public Optional<Attribute> get(UUID id) {
        return attributeRepository.findById(id);
    }
}
