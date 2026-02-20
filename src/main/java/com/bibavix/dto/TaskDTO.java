package com.bibavix.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Schema(description = "Data Transfer Object for Task operations")
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    @Schema(description = "Title of the task", example = "Complete report", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Schema(description = "Description of the task", example = "Monthly sales report")
    private String description;

    private UUID priorityId;

    private UUID taskId;

    private UUID userId;

    private UUID categoryId;

    private UUID statusId;

    private String dueDate; // Usamos String para recibir la fecha en formato ISO (ej. "2025-06-01")

    private String createdAt;

    private String updatedAt;
}
