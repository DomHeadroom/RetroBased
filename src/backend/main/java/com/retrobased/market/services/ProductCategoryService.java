package com.retrobased.market.services;

import com.retrobased.market.entities.Product;
import com.retrobased.market.entities.Category;
import com.retrobased.market.entities.ProductCategory;
import com.retrobased.market.repositories.ProductCategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    public void create(Category category, Product product) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProduct(product);
        productCategory.setCategory(category);
        productCategoryRepository.save(productCategory);
    }
}
