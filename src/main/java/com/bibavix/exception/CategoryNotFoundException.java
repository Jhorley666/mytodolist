package com.bibavix.exception;

public class CategoryNotFoundException  extends RuntimeException{
    public CategoryNotFoundException(Integer taskId) {
        super("Category not found for ID: " + taskId);
    }
}
