package com.bibavix.service;

import com.bibavix.dto.TaskDTO;
import com.bibavix.model.Task;

import java.util.List;

public interface TaskService {
    Task createTask(TaskDTO taskDTO, String username);
    Task findTaskById(Integer taskId);
    TaskDTO updateTask(Integer taskId, TaskDTO taskDTO, String username);
    void deleteTask(Integer taskId, String username);
    List<TaskDTO> getAllTasksByUser(String username);
}
