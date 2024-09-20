package com.retrobased.market.services;

import com.retrobased.market.entities.Category;
import com.retrobased.market.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public boolean exists(UUID id) {
        return categoryRepository.existsById(id);
    }

    public Category get(UUID id) {
        return categoryRepository.getReferenceById(id);
    }

    public boolean areCategoriesValid(Category firstCategory, Category secondCategory) {
        return secondCategory.getParentCategory().getId().equals(firstCategory.getId()) ||
                firstCategory.getParentCategory().getId().equals(secondCategory.getId());
    }
}
