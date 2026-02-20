package com.bibavix.service;

import java.util.UUID;

import com.bibavix.dto.TaskDTO;
import com.bibavix.model.Task;

import java.util.List;

public interface TaskService {
    Task createTask(TaskDTO taskDTO, String username);

    Task findTaskById(UUID taskId);

    TaskDTO updateTask(UUID taskId, TaskDTO taskDTO, String username);

    void deleteTask(UUID taskId, String username);

    List<TaskDTO> getAllTasksByUser(String username);
}
