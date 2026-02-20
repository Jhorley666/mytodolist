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
import java.util.UUID;

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

    private final UUID categoryId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private final UUID diffCategoryId = UUID.fromString("00000000-0000-0000-0000-000000000099");
    private final UUID diffUserId = UUID.fromString("00000000-0000-0000-0000-000000000099");

    @BeforeEach
    void setUp() {
        categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(categoryId);
        categoryDTO.setUserId(userId);
        categoryDTO.setName("Work");

        category = new Category();
        category.setCategoryId(categoryId);
        category.setUserId(userId);
        category.setName("Work");
    }

    @Test
    void addCategory_shouldSaveAndReturnId() {
        when(categoryMapper.toEntity(categoryDTO)).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        UUID result = categoryService.addCategory(categoryDTO, userId);

        assertEquals(categoryId, result);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void getAllCategoriesByUserId_shouldReturnMappedList() {
        when(categoryRepository.findAllByUserId(userId)).thenReturn(List.of(category));
        when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);

        List<CategoryDTO> result = categoryService.getAllCategoriesByUserId(userId);

        assertEquals(1, result.size());
        assertEquals(categoryDTO, result.get(0));
    }

    @Test
    void updateCategory_shouldUpdateAndReturnDTO() {
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);
        when(categoryRepository.save(category)).thenReturn(category);

        CategoryDTO updatedDTO = categoryService.updateCategory(categoryDTO);

        assertEquals(categoryDTO, updatedDTO);
        verify(categoryMapper).updateCategoryFromDTO(categoryDTO, category);
        verify(categoryRepository).save(category);
    }

    @Test
    void updateCategory_shouldThrowIfNotAuthorized() {
        categoryDTO.setUserId(diffUserId); // Different user
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        assertThrows(SecurityException.class, () -> categoryService.updateCategory(categoryDTO));
    }

    @Test
    void updateCategory_shouldThrowIfNotFound() {
        categoryDTO.setCategoryId(diffCategoryId);
        when(categoryRepository.existsById(diffCategoryId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> categoryService.updateCategory(categoryDTO));
    }

    @Test
    void deleteCategory_shouldDeleteIfValid() {
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        Category categoryU = new Category();
        categoryU.setUserId(userId);
        categoryU.setCategoryId(categoryId);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryU));
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setUserId(userId);
        categoryDTO.setCategoryId(categoryId);
        categoryService.deleteCategory(categoryDTO);

        verify(categoryRepository).deleteById(categoryId);
    }

    @Test
    void deleteCategory_shouldThrowIfNotAuthorized() {
        categoryDTO.setUserId(diffUserId);
        Category categoryU = new Category();
        categoryU.setUserId(userId);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryU));
        when(categoryRepository.existsById(categoryDTO.getCategoryId())).thenReturn(Boolean.TRUE);
        assertThrows(SecurityException.class, () -> categoryService.deleteCategory(categoryDTO));
    }

    @Test
    void validateCategory_shouldThrowIfNotFound() {
        when(categoryRepository.existsById(categoryId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> categoryService.validateCategory(categoryDTO));
    }

    @Test
    void validateCategoryByUser_shouldThrowIfUserMismatch() {
        assertThrows(SecurityException.class, () -> categoryService.validateCategoryByUser(categoryDTO, diffUserId));
    }
}