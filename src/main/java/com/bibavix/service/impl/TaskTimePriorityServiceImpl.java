package com.bibavix.service.impl;

import com.bibavix.dto.TaskTimePriorityDTO;
import java.util.UUID;
import com.bibavix.exception.ResourceNotFoundException;
import com.bibavix.model.TaskTimePriority;
import com.bibavix.model.User;
import com.bibavix.repository.TaskTimePriorityRepository;
import com.bibavix.service.TaskTimePriorityService;
import com.bibavix.util.mapper.TaskTimePriorityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskTimePriorityServiceImpl implements TaskTimePriorityService {

    private final TaskTimePriorityRepository taskTimePriorityRepository;
    private final TaskTimePriorityMapper taskTimePriorityMapper;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public TaskTimePriorityDTO createTaskTimePriority(TaskTimePriorityDTO taskTimePriorityDTO, String username) {
        User user = userDetailsService.findUserByUsername(username);
        taskTimePriorityDTO.setUserId(user.getUserId());
        TaskTimePriority taskTimePriorityToSave = taskTimePriorityMapper.toEntity(taskTimePriorityDTO);
        TaskTimePriority taskTimePriority = taskTimePriorityRepository.save(taskTimePriorityToSave);
        return taskTimePriorityMapper.toDTO(taskTimePriority);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TaskTimePriorityDTO> getAllTaskTimePriorities() {
        List<TaskTimePriority> taskTimePriorityList = taskTimePriorityRepository.findAll();
        return taskTimePriorityList.stream()
                .map(taskTimePriorityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskTimePriorityDTO getTaskTimePriorityById(UUID taskTimePriorityId, String username) {
        User user = userDetailsService.findUserByUsername(username);
        TaskTimePriority taskTimePriority = taskTimePriorityRepository.findByPriorityIdAndUserId(taskTimePriorityId,
                user.getUserId());
        if (Objects.isNull(taskTimePriority))
            throw new ResourceNotFoundException(
                    "TaskTimePriority with id " + taskTimePriorityId + " not found.");
        return taskTimePriorityMapper.toDTO(taskTimePriority);
    }

    @Override
    public TaskTimePriorityDTO getTaskTimePriorityByPriorityId(UUID priorityId, String username) {
        User user = userDetailsService.findUserByUsername(username);
        TaskTimePriority taskTimePriority = taskTimePriorityRepository.findByPriorityIdAndUserId(priorityId,
                user.getUserId());
        if (Objects.isNull(taskTimePriority)) {
            throw new ResourceNotFoundException("TaskTimePriority with priority id " + priorityId + " not found.");
        }
        return taskTimePriorityMapper.toDTO(taskTimePriority);
    }

    @Override
    public TaskTimePriorityDTO updateTaskTimePriority(TaskTimePriorityDTO taskTimePriorityDTO, String username) {
        UUID taskTimePriorityId = taskTimePriorityDTO.getTaskTimePriorityId();
        User user = userDetailsService.findUserByUsername(username);
        TaskTimePriority taskTimePriority = taskTimePriorityRepository.findByPriorityIdAndUserId(taskTimePriorityId,
                user.getUserId());

        if (Objects.isNull(taskTimePriority))
            throw new ResourceNotFoundException(
                    "TaskTimePriority with id " + taskTimePriorityId + " not found.");

        taskTimePriorityMapper.updateTaskTimePriorityFromDTO(taskTimePriorityDTO, taskTimePriority);
        TaskTimePriority updatedTaskTimePriority = taskTimePriorityRepository.save(taskTimePriority);
        return taskTimePriorityMapper.toDTO(updatedTaskTimePriority);
    }

    @Override
    public void deleteTaskTimePriorityById(UUID taskTimePriorityId, String username) {
        getTaskTimePriorityById(taskTimePriorityId, username);
        taskTimePriorityRepository.deleteById(taskTimePriorityId);
    }

    @Override
    public List<TaskTimePriorityDTO> getTaskTimePrioritiesByUser(String username) {
        User user = userDetailsService.findUserByUsername(username);
        List<TaskTimePriority> taskTimePriorityList = taskTimePriorityRepository.findByUserId(user.getUserId());
        return taskTimePriorityList.stream()
                .map(taskTimePriorityMapper::toDTO)
                .collect(Collectors.toList());
    }

}
