package com.bibavix.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskTimePriorityDTO {

    @NotBlank
    private Integer taskTimePriorityId;
    @NotBlank
    private Long time;
    @NotBlank
    private Integer priorityId;
    @NotBlank
    private Integer userId;

}
