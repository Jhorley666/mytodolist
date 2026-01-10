package com.bibavix.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTimerDTO {

    private Integer idUserTimer;
    private Integer userId;
    private Long totalSecondsAccumulated;
    private Long remainingSeconds;
    private Timestamp startedAt;
    private Boolean isRunning;
    private Timestamp updatedAt;

}
