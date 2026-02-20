package com.bibavix.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskTimePriorityDTO {

    @NotBlank
    private UUID taskTimePriorityId;
    @NotBlank
    private Long time;
    @NotBlank
    private UUID priorityId;
    @NotBlank
    private UUID userId;

}
