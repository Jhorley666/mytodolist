package com.bibavix.service;

import com.bibavix.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    Integer addCategory(CategoryDTO categoryDTO, Integer userId);
    List<CategoryDTO> getAllCategoriesByUserId(Integer userId);
    CategoryDTO getCategoryById(Integer categoryId);
    CategoryDTO updateCategory(CategoryDTO categoryDTO);
    void deleteCategory(CategoryDTO categoryDTO);
}
