package com.bibavix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusDTO {
    @NotBlank(message = "Status id is requeried")
    Integer statusId;

    @NotBlank(message = "Name is requeried")
    @Size(max = 20, message = "Name must not exceed 20 characters")
    String name;
}
