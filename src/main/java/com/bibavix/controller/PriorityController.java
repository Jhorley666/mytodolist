package com.bibavix.controller;

import com.bibavix.dto.PriorityDTO;
import com.bibavix.dto.ResponseCode;
import com.bibavix.model.Priority;
import com.bibavix.service.PriorityService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Priorities", description = "Priorities management APIs")
@RestController
@RequestMapping("/v1/priorities")
@RequiredArgsConstructor
public class PriorityController {

    private final PriorityService priorityService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PriorityDTO> createPriority(
            @Parameter(description = "Priority data", required = true, schema = @Schema(implementation = Priority.class))
            @RequestBody PriorityDTO priorityDTO
    ) {
        PriorityDTO createdPriority = priorityService.createPriority(priorityDTO);
        return ResponseEntity.ok(createdPriority);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<PriorityDTO>> getAllPriorities() {
        List<PriorityDTO> priorities = priorityService.getAllPriorities();
        return ResponseEntity.ok(priorities);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PriorityDTO> getPriorityById(
            @Parameter(description = "Priority ID", required = true) @PathVariable Integer id
    ) {
        PriorityDTO priority = priorityService.getPriorityById(id);
        return ResponseEntity.ok(priority);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PriorityDTO> updatePriority(
            @Parameter(description = "Priority ID", required = true) @PathVariable Integer id,
            @Parameter(description = "Priority data", required = true, schema = @Schema(implementation = Priority.class))
            @RequestBody PriorityDTO priorityDTO
    ) {
        priorityDTO.setPriorityId(id);
        PriorityDTO updatedPriority = priorityService.updatePriority(priorityDTO);
        return ResponseEntity.ok(updatedPriority);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseCode> deletePriority(
            @Parameter(description = "Priority ID", required = true) @PathVariable Integer id
    ) {
        priorityService.deletePriority(id);
        return ResponseEntity.ok().build();
    }
}

