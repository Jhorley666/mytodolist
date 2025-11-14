package com.bibavix.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class TaskNotFoundExceptionTest {

    @Test
    void constructor_setsCorrectMessage() {
        int taskId = 42;
        ResourceNotFoundException exception = new ResourceNotFoundException("Task with id " + taskId + " not found.");
        assertEquals("Task with id 42 not found.", exception.getMessage());
    }

    @Test
    void constructor_withNullTaskId_setsCorrectMessage() {
        ResourceNotFoundException exception = new ResourceNotFoundException(null);
        assertNull(exception.getMessage());
    }
}