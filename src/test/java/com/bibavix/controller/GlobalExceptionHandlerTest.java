package com.bibavix.controller;

import com.bibavix.exception.TaskNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.http.HttpStatus;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleValidationExceptions() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        var bindingResult = mock(org.springframework.validation.BindingResult.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        var fieldError = mock(org.springframework.validation.FieldError.class);
        when(fieldError.getField()).thenReturn("title");
        when(fieldError.getDefaultMessage()).thenReturn("must not be blank");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<Map<String, Object>> response = handler.handleValidationExceptions(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation Failed", response.getBody().get("error"));
        Map<String, Object> errors = (Map<String, Object>) response.getBody().get("message");
        assertEquals("must not be blank", errors.get("title"));
    }

    @Test
    void testHandleConstraintViolation() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must be positive");
        Set<ConstraintViolation<?>> violations = Set.of(violation);
        ConstraintViolationException ex = new ConstraintViolationException(violations);

        ResponseEntity<Map<String, Object>> response = handler.handleConstraintViolation(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation Failed", response.getBody().get("error"));
        Map<String, Object> errors = (Map<String, Object>) response.getBody().get("message");
        assertEquals("must be positive", errors.get(path.toString()));
    }

    @Test
    void testHandleTaskNotFoundException() {
        TaskNotFoundException ex = new TaskNotFoundException(123);
        ResponseEntity<Map<String, Object>> response = handler.handleTaskNotFoundException(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", response.getBody().get("error"));
        assertEquals("Task not found for ID: 123", response.getBody().get("message"));
    }

    @Test
    void testHandleSecurityException() {
        SecurityException ex = new SecurityException("Access denied");
        ResponseEntity<Map<String, Object>> response = handler.handleSecurityException(ex);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Forbidden", response.getBody().get("error"));
        assertEquals("Access denied", response.getBody().get("message"));
    }

    @Test
    void testHandleGenericException() {
        Exception ex = new Exception("Something went wrong");
        ResponseEntity<Map<String, Object>> response = handler.handleGenericException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal Server Error", response.getBody().get("error"));
        assertEquals("An unexpected error occurred", response.getBody().get("message"));
    }
}