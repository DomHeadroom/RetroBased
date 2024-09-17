package com.retrobased.market.services;

import com.retrobased.market.entities.Category;
import com.retrobased.market.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {

        this.categoryRepository = categoryRepository;
    }

    public boolean existsById(UUID categoryId) {
        return categoryRepository.existsById(categoryId);
    }

    public Category getById(UUID categoryId) {
        return categoryRepository.getReferenceById(categoryId);
    }

    public boolean areCategoriesValid(Category firstCategory, Category secondCategory) {
        return secondCategory.getParentCategory().getId().equals(firstCategory.getId()) ||
                firstCategory.getParentCategory().getId().equals(secondCategory.getId());
    }
}
