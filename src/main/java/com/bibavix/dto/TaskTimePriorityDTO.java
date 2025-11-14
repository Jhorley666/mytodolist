package com.bibavix.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskTimePriorityDTO {

    @NotBlank
    private Integer taskTimePriorityId;
    @NotBlank
    private Date time;
    @NotBlank
    private Integer priorityId;

}
