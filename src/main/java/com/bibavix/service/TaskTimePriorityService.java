package com.bibavix.service;

import java.util.UUID;

import com.bibavix.dto.TaskTimePriorityDTO;

import java.util.List;

public interface TaskTimePriorityService {
    TaskTimePriorityDTO createTaskTimePriority(TaskTimePriorityDTO taskTimePriorityDTO, String username);

    List<TaskTimePriorityDTO> getAllTaskTimePriorities();

    TaskTimePriorityDTO getTaskTimePriorityById(UUID taskTimePriorityId, String username);

    TaskTimePriorityDTO getTaskTimePriorityByPriorityId(UUID priorityId, String username);

    TaskTimePriorityDTO updateTaskTimePriority(TaskTimePriorityDTO taskTimePriorityDTO, String username);

    void deleteTaskTimePriorityById(UUID taskTimePriorityId, String username);

    List<TaskTimePriorityDTO> getTaskTimePrioritiesByUser(String username);
}
