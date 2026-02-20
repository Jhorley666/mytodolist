package com.bibavix.controller;

import com.bibavix.dto.CategoryDTO;
import com.bibavix.model.User;
import com.bibavix.repository.UserRepository;
import com.bibavix.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {
    @InjectMocks
    CategoryController categoryController;

    @Mock
    UserDetails userDetails;

    @Mock
    UserRepository userRepository;

    @Mock
    CategoryService categoryService;

    @Mock
    User user;

    @Mock
    CategoryDTO categoryDTO;

    @Test
    void shouldReturnNotNullCategoryWhenFindById() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(categoryService.getCategoryById(id)).thenReturn(categoryDTO);
        ResponseEntity<CategoryDTO> response = categoryController.getCategoryById(id, userDetails);

        // Asserts
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(categoryDTO, response.getBody());
    }

}