package com.bibavix.controller;

import com.bibavix.dto.TaskDTO;
import com.bibavix.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<String> createTask(@RequestBody TaskDTO taskDTO) {
        Integer userId = 1;
        Integer taskId = taskService.createTask(taskDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Task created successfully with ID: " + taskId);
    }

    @PutMapping
    public String updateTask(@RequestBody TaskDTO taskDTO) {
        return "Task updated successfully!";
    }

    @DeleteMapping
    public String deleteTask(@RequestBody TaskDTO taskDTO) {
        return "";
    }

    @GetMapping("/{id}")
    public TaskDTO getTask(@PathVariable("id") String id) {
        return null; // Replace null with logic to fetch a task by ID
    }

    @GetMapping("/all")
    public String getAllTasks() {
        return "All tasks";
    }
}
