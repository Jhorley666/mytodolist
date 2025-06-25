package com.bibavix.controller;

import com.bibavix.exception.TaskNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String STATUS = "status";
    public static final String ERROR = "error";
    public static final String MESSAGE = "message";

    /**
     * @author jhorley
     * @return ResponseEntity<Map<String, Object>>
     * @description Handle validation errors from @Valid (e.g., TaskDTO)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, HttpStatus.BAD_REQUEST.value());
        response.put(ERROR, "Validation Failed");
        response.put(MESSAGE, errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * @author jhorley
     * @return ResponseEntity<Map<String, Object>>
     * @description Handle constraint violations (e.g., from path variables or query params)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage()));
        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, HttpStatus.BAD_REQUEST.value());
        response.put(ERROR, "Validation Failed");
        response.put(MESSAGE, errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * @author jhorley
     * @return ResponseEntity<Map<String, Object>>
     * @description Handle TaskNotFoundException
     */
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTaskNotFoundException(TaskNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, HttpStatus.NOT_FOUND.value());
        response.put(ERROR, "Not Found");
        response.put(MESSAGE, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * @author jhorley
     * @return ResponseEntity<Map<String, Object>>
     * @description Handle SecurityException (e.g., unauthorized user)
     */
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, Object>> handleSecurityException(SecurityException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, HttpStatus.FORBIDDEN.value());
        response.put(ERROR, "Forbidden");
        response.put(MESSAGE, ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * @author jhorley
     * @return ResponseEntity<Map<String, Object>>
     * @description Handle generic exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put(ERROR, "Internal Server Error");
        response.put(MESSAGE, "An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
