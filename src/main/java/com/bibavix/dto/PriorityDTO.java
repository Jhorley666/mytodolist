package com.bibavix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriorityDTO {
    @NotBlank
    private UUID priorityId;
    @NotBlank
    @Size(min = 4, max = 8, message = "Priority name should be between 3 and 8 characters.")
    private String priorityName;
}
