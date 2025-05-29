package com.bibavix.service;

import com.bibavix.dto.TaskDTO;
import com.bibavix.exception.TaskNotFoundException;
import com.bibavix.model.Task;
import com.bibavix.repository.CategoryRepository;
import com.bibavix.repository.StatusRepository;
import com.bibavix.repository.TaskRepository;
import com.bibavix.util.mapper.TaskMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final StatusRepository statusRepository;
    private final TaskMapper taskMapper;

    @Transactional
    public Integer createTask(TaskDTO taskDTO, Integer userId) {
        validateCategoryAndStatus(taskDTO);
        Task task = taskMapper.toEntity(taskDTO);
        task.setUserId(userId);
        task.setStatusId(Objects.nonNull(taskDTO.getStatusId())  ? taskDTO.getStatusId().shortValue() : (short) 1);
        Task savedTask = taskRepository.save(task);
        return savedTask.getTaskId();
    }


    @Transactional
    public TaskDTO updateTask(Integer taskId, TaskDTO taskDTO, Integer userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        if (!task.getUserId().equals(userId)) {
            throw new SecurityException("User not authorized to update task");
        }

        validateCategoryAndStatus(taskDTO);
        taskMapper.updateTaskFromDTO(taskDTO, task);
        Task updatedTask = taskRepository.save(task);

        return taskMapper.toDTO(updatedTask);
    }

    @Transactional
    public void deleteTask(Integer taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException(taskId);
        }
        taskRepository.deleteById(taskId);
    }

    @Transactional(readOnly = true)
    public List<TaskDTO> getAllTasksByUserId(Integer userId) {
        List<Task> tasks = taskRepository.findAllByUserId(userId);
        return tasks.stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    private void validateCategoryAndStatus(TaskDTO taskDTO) {
        if (taskDTO.getCategoryId() != null && !categoryRepository.existsById(taskDTO.getCategoryId())) {
            throw new IllegalArgumentException("Category not found for ID: " + taskDTO.getCategoryId());
        }
        if (taskDTO.getStatusId() != null && !statusRepository.existsById(taskDTO.getStatusId())) {
            throw new IllegalArgumentException("Status not found for ID: " + taskDTO.getStatusId());
        }
    }


}
