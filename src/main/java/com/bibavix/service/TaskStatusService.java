package com.bibavix.service;

import com.bibavix.dto.TaskStatusDTO;

import java.util.List;

public interface TaskStatusService {
    TaskStatusDTO createTaskStatus(TaskStatusDTO taskStatusDTO);
    List<TaskStatusDTO> getAllTaskStatus();
    TaskStatusDTO getTaskStatusById(Integer taskStatusId);
    void deleteTaskStatus(Integer taskStatusId);

}
