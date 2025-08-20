package com.bibavix.service.impl;

import com.bibavix.dto.TaskDTO;
import com.bibavix.exception.TaskNotFoundException;
import com.bibavix.model.Task;
import com.bibavix.model.User;
import com.bibavix.repository.CategoryRepository;
import com.bibavix.repository.StatusRepository;
import com.bibavix.repository.TaskRepository;
import com.bibavix.service.TaskService;
import com.bibavix.util.mapper.TaskMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final StatusRepository statusRepository;
    private final TaskMapper taskMapper;
    private final UserDetailsServiceImpl userDetailsService;

    @Transactional
    public Task createTask(TaskDTO taskDTO, String username) {
        User user = userDetailsService.findUserByUsername(username);
        validateCategoryAndStatus(taskDTO);
        Task task = taskMapper.toEntity(taskDTO);
        task.setUserId(user.getUserId());
        task.setStatusId(Objects.nonNull(taskDTO.getStatusId())  ? taskDTO.getStatusId().shortValue() : (short) 1);
        return taskRepository.save(task);
    }


    @Transactional
    public TaskDTO updateTask(Integer taskId, TaskDTO taskDTO, String username) {
        User user = userDetailsService.findUserByUsername(username);
        Task task = findTaskById(taskId);
        if (!task.getUserId().equals(user.getUserId())) {
            throw new SecurityException("User not authorized to update task");
        }
        validateCategoryAndStatus(taskDTO);
        taskMapper.updateTaskFromDTO(taskDTO, task);
        Task updatedTask = taskRepository.save(task);

        return taskMapper.toDTO(updatedTask);
    }

    @Transactional
    public void deleteTask(Integer taskId, String username) {
        User user = userDetailsService.findUserByUsername(username);
        Task task = findTaskById(taskId);
        if (!task.getUserId().equals(user.getUserId())) {
            throw new SecurityException("User not authorized to update task");
        }
        taskRepository.deleteById(task.getTaskId());
    }

    @Transactional(readOnly = true)
    public List<TaskDTO> getAllTasksByUser(String username) {
        User user = userDetailsService.findUserByUsername(username);
        List<Task> tasks = taskRepository.findAllByUserId(user.getUserId());
        return tasks.stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    public Task findTaskById(Integer taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException( taskId));
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
