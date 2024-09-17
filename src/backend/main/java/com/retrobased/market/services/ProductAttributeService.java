package com.retrobased.market.services;

import com.retrobased.market.entities.Attribute;
import com.retrobased.market.entities.Product;
import com.retrobased.market.entities.ProductAttribute;
import com.retrobased.market.repositories.ProductAttributeRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductAttributeService {

    private final ProductAttributeRepository productAttributeRepository;

    public ProductAttributeService(ProductAttributeRepository productAttributeRepository) {
        this.productAttributeRepository = productAttributeRepository;
    }

    public void create(Attribute attribute, Product product) {
        ProductAttribute productAttribute = new ProductAttribute();
        productAttribute.setProduct(product);
        productAttribute.setAttribute(attribute);
        productAttributeRepository.save(productAttribute);
    }
}
