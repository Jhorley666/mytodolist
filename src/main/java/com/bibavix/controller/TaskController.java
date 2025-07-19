package com.bibavix.controller;

import com.bibavix.dto.ResponseCode;
import com.bibavix.model.Task;
import com.bibavix.model.User;
import com.bibavix.repository.TaskRepository;
import com.bibavix.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tasks", description = "Task management APIs")
@RestController
@RequestMapping("/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    public static final String USER_NOT_FOUND = "User not found";
    public static final String TASK_NOT_FOUND = "Task not found";
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Operation(
        summary = "Get all tasks for the authenticated user",
        description = "Returns a list of all tasks belonging to the authenticated user."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of tasks returned",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class)))
    })
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Task>> getAllTasks(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
        List<Task> tasks = taskRepository.findAllByUserId(user.getUserId());
        return ResponseEntity.ok(tasks);
    }

    @Operation(
        summary = "Get a task by ID",
        description = "Returns a task by its ID, only if it belongs to the authenticated user."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Task> getTaskById(
            @Parameter(description = "Task ID", required = true) @PathVariable Integer id,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(TASK_NOT_FOUND));
        if (!task.getUserId().equals(user.getUserId())) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(task);
    }

    @Operation(
        summary = "Create a new task",
        description = "Creates a new task for the authenticated user."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task created",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Task> createTask(
            @Parameter(description = "Task to create", required = true, schema = @Schema(implementation = Task.class))
            @RequestBody Task task,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
        task.setUserId(user.getUserId());
        Task savedTask = taskRepository.save(task);
        return ResponseEntity.ok(savedTask);
    }

    @Operation(
        summary = "Update a task",
        description = "Updates a task by its ID, only if it belongs to the authenticated user."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task updated",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Task> updateTask(
            @Parameter(description = "Task ID", required = true) @PathVariable Integer id,
            @Parameter(description = "Updated task", required = true, schema = @Schema(implementation = Task.class))
            @RequestBody Task updatedTask,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(TASK_NOT_FOUND));
        if (!task.getUserId().equals(user.getUserId())) {
            return ResponseEntity.status(403).build();
        }
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setStatusId(updatedTask.getStatusId());
        Task savedTask = taskRepository.save(task);
        return ResponseEntity.ok(savedTask);
    }

    @Operation(
        summary = "Delete a task",
        description = "Deletes a task by its ID, only if it belongs to the authenticated user."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task deleted"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseCode> deleteTask(
            @Parameter(description = "Task ID", required = true) @PathVariable Integer id,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(TASK_NOT_FOUND));
        if (!task.getUserId().equals(user.getUserId())) {
            return ResponseEntity.status(403).build();
        }
        taskRepository.delete(task);
        return ResponseEntity.ok().build();
    }
}
