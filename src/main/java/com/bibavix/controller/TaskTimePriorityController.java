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
            @Parameter(description = "TaskTimePriority data", required = true, schema = @Schema(implementation = TaskTimePriority.class))
            @RequestBody TaskTimePriorityDTO taskTimePriorityDTO
    ) {
        TaskTimePriorityDTO createdTaskTimePriority = taskTimePriorityService.createTaskTimePriority(taskTimePriorityDTO);
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
            @Parameter(description = "TaskTimePriority ID", required = true) @PathVariable Integer id
    ) {
        TaskTimePriorityDTO taskTimePriority = taskTimePriorityService.getTaskTimePriorityById(id);
        return ResponseEntity.ok(taskTimePriority);
    }

    @GetMapping("/priority/{priorityId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TaskTimePriorityDTO> getTaskTimePriorityByPriorityId(
            @Parameter(description = "Priority ID", required = true) @PathVariable Integer priorityId
    ) {
        TaskTimePriorityDTO taskTimePriority = taskTimePriorityService.getTaskTimePriorityByPriorityId(priorityId);
        return ResponseEntity.ok(taskTimePriority);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TaskTimePriorityDTO> updateTaskTimePriority(
            @Parameter(description = "TaskTimePriority ID", required = true) @PathVariable Integer id,
            @Parameter(description = "TaskTimePriority data", required = true, schema = @Schema(implementation = TaskTimePriority.class))
            @RequestBody TaskTimePriorityDTO taskTimePriorityDTO
    ) {
        taskTimePriorityDTO.setTaskTimePriorityId(id);
        TaskTimePriorityDTO updatedTaskTimePriority = taskTimePriorityService.updateTaskTimePriority(taskTimePriorityDTO);
        return ResponseEntity.ok(updatedTaskTimePriority);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseCode> deleteTaskTimePriority(
            @Parameter(description = "TaskTimePriority ID", required = true) @PathVariable Integer id
    ) {
        taskTimePriorityService.deleteTaskTimePriorityById(id);
        return ResponseEntity.ok().build();
    }

}
