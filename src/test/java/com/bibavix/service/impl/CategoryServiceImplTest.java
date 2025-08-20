package com.bibavix.service.impl;

import com.bibavix.dto.CategoryDTO;
import com.bibavix.model.Category;
import com.bibavix.repository.CategoryRepository;
import com.bibavix.util.mapper.CategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private CategoryDTO categoryDTO;
    private Category category;

    @BeforeEach
    void setUp() {
        categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(1);
        categoryDTO.setUserId(2);
        categoryDTO.setName("Work");

        category = new Category();
        category.setCategoryId(1);
        category.setUserId(2);
        category.setName("Work");
    }

    @Test
    void addCategory_shouldSaveAndReturnId() {
        when(categoryMapper.toEntity(categoryDTO)).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Integer result = categoryService.addCategory(categoryDTO, 2);

        assertEquals(1, result);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void getAllCategoriesByUserId_shouldReturnMappedList() {
        when(categoryRepository.findAllByUserId(2)).thenReturn(List.of(category));
        when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);

        List<CategoryDTO> result = categoryService.getAllCategoriesByUserId(2);

        assertEquals(1, result.size());
        assertEquals(categoryDTO, result.get(0));
    }

    @Test
    void updateCategory_shouldUpdateAndReturnDTO() {
        when(categoryRepository.existsById(1)).thenReturn(true);
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);
        when(categoryRepository.save(category)).thenReturn(category);

        CategoryDTO updatedDTO = categoryService.updateCategory(categoryDTO);

        assertEquals(categoryDTO, updatedDTO);
        verify(categoryMapper).updateCategoryFromDTO(categoryDTO, category);
        verify(categoryRepository).save(category);
    }

    @Test
    void updateCategory_shouldThrowIfNotAuthorized() {
        categoryDTO.setUserId(99); // Different user
        when(categoryRepository.existsById(1)).thenReturn(true);
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        assertThrows(SecurityException.class, () -> categoryService.updateCategory(categoryDTO));
    }

    @Test
    void updateCategory_shouldThrowIfNotFound() {
        categoryDTO.setCategoryId(99);
        when(categoryRepository.existsById(99)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> categoryService.updateCategory(categoryDTO));
    }

    @Test
    void deleteCategory_shouldDeleteIfValid() {
        when(categoryRepository.existsById(1)).thenReturn(true);
        Category categoryU = new Category();
        categoryU.setUserId(1);
        categoryU.setCategoryId(1);
        when(categoryRepository.findById(1)).thenReturn(Optional.of(categoryU));
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setUserId(1);
        categoryDTO.setCategoryId(1);
        categoryService.deleteCategory(categoryDTO);

        verify(categoryRepository).deleteById(1);
    }

    @Test
    void deleteCategory_shouldThrowIfNotAuthorized() {
        categoryDTO.setUserId(99);
        Category categoryU = new Category();
        categoryU.setUserId(2);
        when(categoryRepository.findById(1)).thenReturn(Optional.of(categoryU));
        when(categoryRepository.existsById(categoryDTO.getCategoryId())).thenReturn(Boolean.TRUE);
        assertThrows(SecurityException.class, () -> categoryService.deleteCategory(categoryDTO));
    }

    @Test
    void validateCategory_shouldThrowIfNotFound() {
        when(categoryRepository.existsById(1)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> categoryService.validateCategory(categoryDTO));
    }

    @Test
    void validateCategoryByUser_shouldThrowIfUserMismatch() {
        assertThrows(SecurityException.class, () -> categoryService.validateCategoryByUser(categoryDTO, 99));
    }
}