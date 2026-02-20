package com.bibavix.service;

import java.util.UUID;

import com.bibavix.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    UUID addCategory(CategoryDTO categoryDTO, UUID userId);

    List<CategoryDTO> getAllCategoriesByUserId(UUID userId);

    CategoryDTO getCategoryById(UUID categoryId);

    CategoryDTO updateCategory(CategoryDTO categoryDTO);

    void deleteCategory(CategoryDTO categoryDTO);
}
