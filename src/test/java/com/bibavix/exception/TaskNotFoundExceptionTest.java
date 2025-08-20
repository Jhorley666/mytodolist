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
        TaskNotFoundException exception = new TaskNotFoundException(taskId);
        assertEquals("Task not found for ID: 42", exception.getMessage());
    }

    @Test
    void constructor_withNullTaskId_setsCorrectMessage() {
        TaskNotFoundException exception = new TaskNotFoundException(null);
        assertEquals("Task not found for ID: null", exception.getMessage());
    }
}