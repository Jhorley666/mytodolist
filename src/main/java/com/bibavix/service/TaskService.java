package com.bibavix.service;

import com.bibavix.dto.TaskDTO;
import com.bibavix.model.Task;
import com.bibavix.repository.TaskRepository;
import com.bibavix.util.mapper.TaskMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Transactional
    public Integer createTask(TaskDTO taskDTO, Integer userId) {
        Task task = taskMapper.toEntity(taskDTO);
        task.setUserId(userId);
        task.setStatusId(Objects.nonNull(taskDTO.getStatusId())  ? taskDTO.getStatusId().shortValue() : (short) 1);
        Task savedTask = taskRepository.save(task);
        return savedTask.getTaskId();
    }

    @Transactional(readOnly = true)
    public List<TaskDTO> getAllTasksByUserId(Integer userId) {
        List<Task> tasks = taskRepository.findAllByUserId(userId);
        return tasks.stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }

}
