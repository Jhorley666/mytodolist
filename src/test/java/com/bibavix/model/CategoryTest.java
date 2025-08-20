package com.bibavix.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void testCategoryFieldsAndGettersSetters() {
        Category category = new Category();
        category.setCategoryId(10);
        category.setUserId(5);
        category.setName("Work");

        assertEquals(10, category.getCategoryId());
        assertEquals(5, category.getUserId());
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