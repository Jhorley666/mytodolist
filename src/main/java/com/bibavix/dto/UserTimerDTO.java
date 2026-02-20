package com.bibavix.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTimerDTO {

    private UUID idUserTimer;
    private UUID userId;
    private Long totalSecondsAccumulated;
    private Long remainingSeconds;
    private Timestamp startedAt;
    private Boolean isRunning;
    private Timestamp updatedAt;

}
