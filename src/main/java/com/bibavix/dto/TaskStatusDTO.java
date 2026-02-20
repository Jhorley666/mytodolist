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
public class TaskStatusDTO {
    @NotBlank(message = "Status id is requeried")
    UUID statusId;

    @NotBlank(message = "Name is requeried")
    @Size(max = 20, message = "Name must not exceed 20 characters")
    String name;
}
