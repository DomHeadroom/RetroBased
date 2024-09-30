package com.retrobased.market.services;

import com.retrobased.market.entities.Category;
import com.retrobased.market.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Checks if a category with the given ID exists.
     *
     * @param id the ID of the category
     * @return true if the category exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean exists(UUID id) {
        return categoryRepository.existsById(id);
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id the ID of the category
     * @return an Optional containing the category if found, empty otherwise
     */
    @Transactional(readOnly = true)
    public Optional<Category> get(UUID id) {
        return categoryRepository.findById(id);
    }

    public boolean areCategoriesValid(Category firstCategory, Category secondCategory) {
        return secondCategory.getParentCategory().getId().equals(firstCategory.getId()) ||
                firstCategory.getParentCategory().getId().equals(secondCategory.getId());
    }
}
