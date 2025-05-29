package com.bibavix.controller;

import com.bibavix.dto.TaskDTO;
import com.bibavix.exception.TaskNotFoundException;
import com.bibavix.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Task API", description = "Operations related to task management")
@SecurityRequirement(name = "basicAuth")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Create a new task", description = "Creates a new task for the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task created successfully",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    })
    @PostMapping
    public ResponseEntity<String> createTask(@RequestBody TaskDTO taskDTO) {
        Integer userId = getAuthenticatedUserId();
        Integer taskId = taskService.createTask(taskDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Task created successfully with ID: " + taskId);
    }

    @Operation(summary = "Update an existing task",
            description = "Updates the task with the specified ID for the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    })
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable Integer taskId,
            @Valid @RequestBody TaskDTO taskDTO) {
        Integer userId  = getAuthenticatedUserId();
        TaskDTO updatedTaskDTO = taskService.updateTask(taskId, taskDTO, userId);
        return ResponseEntity.ok(updatedTaskDTO);
    }

    @Operation(summary = "Delete a task",
            description = "Deletes the task with the specified ID for the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    })
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get tasks for the authenticated user",
            description = "Retrieves all tasks associated with the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of tasks",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    })
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
