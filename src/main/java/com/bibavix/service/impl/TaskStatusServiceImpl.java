package com.bibavix.service.impl;

import com.bibavix.dto.TaskStatusDTO;
import com.bibavix.exception.TaskStatusNotFoundException;
import com.bibavix.model.TaskStatus;
import com.bibavix.repository.TaskStatusRepository;
import com.bibavix.service.TaskStatusService;
import com.bibavix.util.mapper.TaskStatusMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;
    private final TaskStatusMapper taskStatusMapper;

    @Override
    public TaskStatusDTO createTaskStatus(TaskStatusDTO taskStatusDTO) {
        TaskStatus taskStatusToSave = taskStatusMapper.toEntity(taskStatusDTO);
        TaskStatus taskStatus = taskStatusRepository.save(taskStatusToSave);
        return taskStatusMapper.toDTO(taskStatus);
    }

    @Override
    public List<TaskStatusDTO> getAllTaskStatus() {
        List<TaskStatus> taskStatusList =  taskStatusRepository.findAll();
        return taskStatusList.stream()
                .map(taskStatusMapper::toDTO)
                .toList();
    }

    @Override
    public TaskStatusDTO getTaskStatusById(Integer taskStatusId) {
        TaskStatus taskStatus = taskStatusRepository.findById(taskStatusId)
                .orElseThrow(() ->
                        new TaskStatusNotFoundException(taskStatusId));
        return taskStatusMapper.toDTO(taskStatus);
    }

    @Override
    public void deleteTaskStatus(Integer taskStatusId) {
        getTaskStatusById(taskStatusId);
        taskStatusRepository.deleteById(taskStatusId);
    }
}
