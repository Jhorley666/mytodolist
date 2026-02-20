package com.bibavix.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.util.UUID;

@Data
@Schema(description = "Data Transfer Object for Service operations")
public class CategoryDTO {
    @NotBlank(message = "Name is required")
    @Size(max = 50, min = 3, message = "Name must not exceed 50 characters")
    @Schema(description = "Category of the task", example = "Work", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Id of the category", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID categoryId;

    @Schema(description = "User id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID userId;

    @Schema(description = "Date modification", example = "2025-06-01", requiredMode = Schema.RequiredMode.REQUIRED)
    private String createdAt;
}
