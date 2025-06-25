package com.bibavix.service;

import com.bibavix.dto.TaskDTO;

import java.util.List;

public interface TaskService {
    Integer createTask(TaskDTO taskDTO, Integer userId);
    TaskDTO updateTask(Integer taskId, TaskDTO taskDTO, Integer userId);
    void deleteTask(Integer taskId);
    List<TaskDTO> getAllTasksByUserId(Integer userId);
}
