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
import java.util.UUID;

@Tag(name = "TaskTimePriorities", description = "Task Time Priorities management APIs")
@RestController
@RequestMapping("/v1/tasks-time-priority")
@RequiredArgsConstructor
public class TaskTimePriorityController {

    private final TaskTimePriorityService taskTimePriorityService;

    @PostMapping
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<TaskTimePriorityDTO> createTaskTimePriority(
            @Parameter(description = "TaskTimePriority data", required = true, schema = @Schema(implementation = TaskTimePriority.class)) @RequestBody TaskTimePriorityDTO taskTimePriorityDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        TaskTimePriorityDTO createdTaskTimePriority = taskTimePriorityService
                .createTaskTimePriority(taskTimePriorityDTO, userDetails.getUsername());
        return ResponseEntity.ok(createdTaskTimePriority);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<List<TaskTimePriorityDTO>> getAllTaskTimePriorities() {
        List<TaskTimePriorityDTO> taskTimePriorities = taskTimePriorityService.getAllTaskTimePriorities();
        return ResponseEntity.ok(taskTimePriorities);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<TaskTimePriorityDTO> getTaskTimePriorityById(
            @Parameter(description = "TaskTimePriority ID", required = true) @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {
        TaskTimePriorityDTO taskTimePriority = taskTimePriorityService.getTaskTimePriorityById(id,
                userDetails.getUsername());
        return ResponseEntity.ok(taskTimePriority);
    }

    @GetMapping("/priority/{priorityId}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<TaskTimePriorityDTO> getTaskTimePriorityByPriorityId(
            @Parameter(description = "Priority ID", required = true) @PathVariable UUID priorityId,
            @AuthenticationPrincipal UserDetails userDetails) {
        TaskTimePriorityDTO taskTimePriority = taskTimePriorityService.getTaskTimePriorityByPriorityId(priorityId,
                userDetails.getUsername());
        return ResponseEntity.ok(taskTimePriority);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<TaskTimePriorityDTO> updateTaskTimePriority(
            @Parameter(description = "TaskTimePriority ID", required = true) @PathVariable UUID id,
            @Parameter(description = "TaskTimePriority data", required = true, schema = @Schema(implementation = TaskTimePriority.class)) @RequestBody TaskTimePriorityDTO taskTimePriorityDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        taskTimePriorityDTO.setTaskTimePriorityId(id);
        TaskTimePriorityDTO updatedTaskTimePriority = taskTimePriorityService
                .updateTaskTimePriority(taskTimePriorityDTO, userDetails.getUsername());
        return ResponseEntity.ok(updatedTaskTimePriority);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<ResponseCode> deleteTaskTimePriority(
            @Parameter(description = "TaskTimePriority ID", required = true) @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {
        taskTimePriorityService.deleteTaskTimePriorityById(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<List<TaskTimePriorityDTO>> getTaskTimePrioritiesByUser(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<TaskTimePriorityDTO> taskTimePriorities = taskTimePriorityService
                .getTaskTimePrioritiesByUser(userDetails.getUsername());
        return ResponseEntity.ok(taskTimePriorities);
    }

}
