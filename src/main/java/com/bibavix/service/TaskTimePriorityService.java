package com.bibavix.service;

import com.bibavix.dto.TaskTimePriorityDTO;

import java.util.List;

public interface TaskTimePriorityService {
    TaskTimePriorityDTO createTaskTimePriority(TaskTimePriorityDTO taskTimePriorityDTO);
    List<TaskTimePriorityDTO> getAllTaskTimePriorities();
    TaskTimePriorityDTO getTaskTimePriorityById(Integer taskTimePriorityId);
    TaskTimePriorityDTO getTaskTimePriorityByPriorityId(Integer priorityId);
    TaskTimePriorityDTO updateTaskTimePriority(TaskTimePriorityDTO taskTimePriorityDTO);
    void deleteTaskTimePriorityById(Integer taskTimePriorityId);
}
