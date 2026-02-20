package com.bibavix.service.impl;

import com.bibavix.dto.CategoryDTO;
import java.util.UUID;
import com.bibavix.exception.ResourceNotFoundException;
import com.bibavix.model.Category;
import com.bibavix.repository.CategoryRepository;
import com.bibavix.service.CategoryService;
import com.bibavix.util.mapper.CategoryMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public UUID addCategory(CategoryDTO categoryDTO, UUID userId) {
        Category category = categoryMapper.toEntity(categoryDTO);
        category.setUserId(userId);
        Category savedCategory = categoryRepository.save(category);
        return savedCategory.getCategoryId();
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDTO> getAllCategoriesByUserId(UUID userId) {
        List<Category> categories = categoryRepository.findAllByUserId(userId);
        return categories.stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryById(UUID categoryId) {
        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + categoryId + " not found."));
        return categoryMapper.toDTO(category);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        validateCategory(categoryDTO);
        Category category = categoryRepository
                .findById(categoryDTO.getCategoryId()).orElseThrow();
        validateCategoryByUser(categoryDTO, category.getUserId());
        categoryMapper.updateCategoryFromDTO(categoryDTO, category);
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toDTO(updatedCategory);
    }

    @Override
    public void deleteCategory(CategoryDTO categoryDTO) {
        validateCategory(categoryDTO);
        Category category = categoryRepository
                .findById(categoryDTO.getCategoryId()).orElseThrow();
        validateCategoryByUser(categoryDTO, category.getUserId());
        categoryRepository.deleteById(categoryDTO.getCategoryId());
    }

    public void validateCategory(CategoryDTO categoryDTO) {
        if (categoryDTO.getCategoryId() != null && !categoryRepository.existsById(categoryDTO.getCategoryId())) {
            throw new IllegalArgumentException("Category not found for ID: " + categoryDTO.getCategoryId());
        }
    }

    public void validateCategoryByUser(CategoryDTO categoryDTO, UUID userId) {
        if (!categoryDTO.getUserId().equals(userId)) {
            throw new SecurityException("User not authorized to update category");
        }
    }
}
