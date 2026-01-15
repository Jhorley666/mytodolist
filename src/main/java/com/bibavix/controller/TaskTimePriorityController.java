package com.bibavix.controller;

import com.bibavix.dto.ResponseCode;
import com.bibavix.dto.TaskTimePriorityDTO;
import com.bibavix.model.TaskTimePriority;
import com.bibavix.service.TaskTimePriorityService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "TaskTimePriorities", description = "Task Time Priorities management APIs")
@RestController
@RequestMapping("/v1/tasks-time-priority")
@RequiredArgsConstructor
public class TaskTimePriorityController {

    private final TaskTimePriorityService taskTimePriorityService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TaskTimePriorityDTO> createTaskTimePriority(
            @Parameter(description = "TaskTimePriority data", required = true,
                    schema = @Schema(implementation = TaskTimePriority.class))
            @RequestBody TaskTimePriorityDTO taskTimePriorityDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        TaskTimePriorityDTO createdTaskTimePriority = taskTimePriorityService
                .createTaskTimePriority(taskTimePriorityDTO, userDetails.getUsername());
        return ResponseEntity.ok(createdTaskTimePriority);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TaskTimePriorityDTO>> getAllTaskTimePriorities() {
        List<TaskTimePriorityDTO> taskTimePriorities = taskTimePriorityService.getAllTaskTimePriorities();
        return ResponseEntity.ok(taskTimePriorities);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TaskTimePriorityDTO> getTaskTimePriorityById(
            @Parameter(description = "TaskTimePriority ID", required = true)
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        TaskTimePriorityDTO taskTimePriority = taskTimePriorityService.getTaskTimePriorityById(id, userDetails.getUsername());
        return ResponseEntity.ok(taskTimePriority);
    }

    @GetMapping("/priority/{priorityId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TaskTimePriorityDTO> getTaskTimePriorityByPriorityId(
            @Parameter(description = "Priority ID", required = true)
            @PathVariable Integer priorityId,
            @AuthenticationPrincipal UserDetails userDetails) {
        TaskTimePriorityDTO taskTimePriority = taskTimePriorityService.getTaskTimePriorityByPriorityId(priorityId, userDetails.getUsername());
        return ResponseEntity.ok(taskTimePriority);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TaskTimePriorityDTO> updateTaskTimePriority(
            @Parameter(description = "TaskTimePriority ID", required = true)
            @PathVariable Integer id,
            @Parameter(description = "TaskTimePriority data", required = true,
                    schema = @Schema(implementation = TaskTimePriority.class))
            @RequestBody TaskTimePriorityDTO taskTimePriorityDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        taskTimePriorityDTO.setTaskTimePriorityId(id);
        TaskTimePriorityDTO updatedTaskTimePriority = taskTimePriorityService
                .updateTaskTimePriority(taskTimePriorityDTO, userDetails.getUsername());
        return ResponseEntity.ok(updatedTaskTimePriority);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseCode> deleteTaskTimePriority(
            @Parameter(description = "TaskTimePriority ID", required = true)
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        taskTimePriorityService.deleteTaskTimePriorityById(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TaskTimePriorityDTO>> getTaskTimePrioritiesByUser(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<TaskTimePriorityDTO> taskTimePriorities = taskTimePriorityService.getTaskTimePrioritiesByUser(userDetails.getUsername());
        return ResponseEntity.ok(taskTimePriorities);
    }

}
