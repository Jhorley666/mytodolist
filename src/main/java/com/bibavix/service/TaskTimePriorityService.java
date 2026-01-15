package com.bibavix.service;

import com.bibavix.dto.TaskTimePriorityDTO;

import java.util.List;

public interface TaskTimePriorityService {
    TaskTimePriorityDTO createTaskTimePriority(TaskTimePriorityDTO taskTimePriorityDTO, String username);

    List<TaskTimePriorityDTO> getAllTaskTimePriorities();

    TaskTimePriorityDTO getTaskTimePriorityById(Integer taskTimePriorityId, String username);

    TaskTimePriorityDTO getTaskTimePriorityByPriorityId(Integer priorityId, String username);

    TaskTimePriorityDTO updateTaskTimePriority(TaskTimePriorityDTO taskTimePriorityDTO, String username);

    void deleteTaskTimePriorityById(Integer taskTimePriorityId, String username);

    List<TaskTimePriorityDTO> getTaskTimePrioritiesByUser(String username);
}
