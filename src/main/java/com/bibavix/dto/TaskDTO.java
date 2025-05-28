package com.bibavix.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Data
public class TaskDTO {
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Pattern(regexp = "Low|Medium|High", message = "Priority must be 'Low', 'Medium', or 'High'")
    private String priority = "Medium";

    private Integer categoryId;

    private Integer statusId;

    private String dueDate; // Usamos String para recibir la fecha en formato ISO (ej. "2025-06-01")
}
