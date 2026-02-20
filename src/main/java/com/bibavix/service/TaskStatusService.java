package com.bibavix.service;

import java.util.UUID;

import com.bibavix.dto.TaskStatusDTO;

import java.util.List;

public interface TaskStatusService {
    TaskStatusDTO createTaskStatus(TaskStatusDTO taskStatusDTO);

    List<TaskStatusDTO> getAllTaskStatus();

    TaskStatusDTO getTaskStatusById(UUID taskStatusId);

    void deleteTaskStatus(UUID taskStatusId);

}
