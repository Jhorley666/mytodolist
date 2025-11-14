package com.bibavix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriorityDTO {
    @NotBlank
    private Integer priorityId;
    @NotBlank
    @Size(min = 4, max = 8, message = "Priority name should be between 3 and 8 characters.")
    private String priorityName;
}
