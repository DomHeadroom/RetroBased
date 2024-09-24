package com.retrobased.market.services;

import com.retrobased.market.entities.Product;
import com.retrobased.market.entities.ProductTag;
import com.retrobased.market.entities.Tag;
import com.retrobased.market.repositories.ProductTagRepository;
import com.retrobased.market.repositories.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProductTagService {

    private final TagRepository tagRepository;
    private final ProductTagRepository productTagRepository;

    public ProductTagService(TagRepository tagRepository, ProductTagRepository productTagRepository) {
        this.tagRepository = tagRepository;
        this.productTagRepository = productTagRepository;
    }

    /**
     * Checks if a tag with the given ID exists.
     *
     * @param id the ID of the tag
     * @return true if the tag exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean exists(UUID id) {
        return tagRepository.existsById(id);
    }

    /**
     * Retrieves a tag by its ID.
     *
     * @param id the ID of the tag
     * @return an Optional containing the tag if found, empty otherwise
     */
    @Transactional(readOnly = true)
    public Optional<Tag> get(UUID id) {
        return tagRepository.findById(id);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void create(Tag tag, Product product) {
        ProductTag productTag = new ProductTag();

        ProductTag.ProductTagId productTagId = new ProductTag.ProductTagId(
                product.getId(),
                tag.getId()
        );

        productTag.setProduct(product);
        productTag.setTag(tag);
        productTag.setId(productTagId);
        productTagRepository.save(productTag);
    }
}
