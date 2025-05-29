package com.bibavix.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Integer taskId) {
        super("Task not found for ID: " + taskId);
    }
}
