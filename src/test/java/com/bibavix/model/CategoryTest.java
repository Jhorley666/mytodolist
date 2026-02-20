package com.bibavix.model;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void testCategoryFieldsAndGettersSetters() {
        UUID categoryId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setUserId(userId);
        category.setName("Work");

        assertEquals(categoryId, category.getCategoryId());
        assertEquals(userId, category.getUserId());
        assertEquals("Work", category.getName());
    }

    @Test
    void testDefaultConstructor() {
        Category category = new Category();
        assertNull(category.getCategoryId());
        assertNull(category.getUserId());
        assertNull(category.getName());
    }
}