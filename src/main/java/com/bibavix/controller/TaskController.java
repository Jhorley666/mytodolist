package com.bibavix.controller;

import com.bibavix.dto.TaskDTO;
import com.bibavix.exception.TaskNotFoundException;
import com.bibavix.service.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<String> createTask(@RequestBody TaskDTO taskDTO) {
        Integer userId = getAuthenticatedUserId();
        Integer taskId = taskService.createTask(taskDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Task created successfully with ID: " + taskId);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable Integer taskId,
            @Valid @RequestBody TaskDTO taskDTO) {
        Integer userId  = getAuthenticatedUserId();
        TaskDTO updatedTaskDTO = taskService.updateTask(taskId, taskDTO, userId);
        return ResponseEntity.ok(updatedTaskDTO);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user")
    public ResponseEntity<List<TaskDTO>> getAllTasksByUserId() {
        Integer userId  = getAuthenticatedUserId();
        List<TaskDTO> tasks = taskService.getAllTasksByUserId(userId);
        return ResponseEntity.ok(tasks);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleException(TaskNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleSecurityException(SecurityException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    private Integer getAuthenticatedUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return "juanperez".equals(username) ? 1 : null;
    }

}
