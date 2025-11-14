package com.bibavix.service.impl;

import com.bibavix.dto.TaskTimePriorityDTO;
import com.bibavix.exception.ResourceNotFoundException;
import com.bibavix.model.TaskTimePriority;
import com.bibavix.repository.TaskTimePriorityRepository;
import com.bibavix.service.TaskTimePriorityService;
import com.bibavix.util.mapper.TaskTimePriorityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskTimePriorityServiceImpl implements TaskTimePriorityService {

    private final TaskTimePriorityRepository taskTimePriorityRepository;
    private final TaskTimePriorityMapper taskTimePriorityMapper;

    @Override
    public TaskTimePriorityDTO createTaskTimePriority(TaskTimePriorityDTO taskTimePriorityDTO){
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
                .toList();
    }

    @Override
    public TaskTimePriorityDTO getTaskTimePriorityById(Integer taskTimePriorityId) {
        TaskTimePriority taskTimePriority = taskTimePriorityRepository.findById(taskTimePriorityId)
                .orElseThrow(() -> new ResourceNotFoundException("TaskTimePriority with id " + taskTimePriorityId + " not found."));
        return taskTimePriorityMapper.toDTO(taskTimePriority);
    }

    @Override
    public TaskTimePriorityDTO getTaskTimePriorityByPriorityId(Integer priorityId) {
        TaskTimePriority taskTimePriority = taskTimePriorityRepository.findByPriorityId(priorityId);
        if (taskTimePriority == null) {
            throw new ResourceNotFoundException("TaskTimePriority with priority id " + priorityId + " not found.");
        }
        return taskTimePriorityMapper.toDTO(taskTimePriority);
    }

    @Override
    public TaskTimePriorityDTO updateTaskTimePriority(TaskTimePriorityDTO taskTimePriorityDTO) {
        Integer taskTimePriorityId = taskTimePriorityDTO.getTaskTimePriorityId();
        TaskTimePriority taskTimePriority = taskTimePriorityRepository.findById(taskTimePriorityId)
                .orElseThrow(() -> new ResourceNotFoundException("TaskTimePriority with id " + taskTimePriorityId + " not found."));
        taskTimePriorityMapper.updateTaskTimePriorityFromDTO(taskTimePriorityDTO, taskTimePriority);
        TaskTimePriority updatedTaskTimePriority = taskTimePriorityRepository.save(taskTimePriority);
        return taskTimePriorityMapper.toDTO(updatedTaskTimePriority);
    }

    @Override
    public void deleteTaskTimePriorityById(Integer taskTimePriorityId) {
        getTaskTimePriorityById(taskTimePriorityId);
        taskTimePriorityRepository.deleteById(taskTimePriorityId);
    }

}
