package com.bibavix.service.impl;

import com.bibavix.dto.TaskDTO;
import com.bibavix.exception.ResourceNotFoundException;
import com.bibavix.model.Task;
import com.bibavix.model.TaskStatus;
import com.bibavix.model.User;
import com.bibavix.repository.CategoryRepository;
import com.bibavix.repository.StatusRepository;
import com.bibavix.repository.TaskRepository;
import com.bibavix.service.TaskService;
import com.bibavix.service.UserTimerService;
import com.bibavix.util.mapper.TaskMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Log4j2
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final StatusRepository statusRepository;
    private final TaskMapper taskMapper;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserTimerService userTimerService;

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

        TaskStatus previousStatus = new TaskStatus();
        previousStatus.setStatusId(Integer.valueOf(task.getStatusId()));

        if (previousStatus.getStatusId() == 3 && taskDTO.getStatusId() != 3) {
            throw new IllegalStateException("Completed tasks cannot be modified");
        }

        validateCategoryAndStatus(taskDTO);
        taskMapper.updateTaskFromDTO(taskDTO, task);
        Task updatedTask = taskRepository.save(task);

        boolean transitionedToCompleted =
                previousStatus.getStatusId() != 3 &&
                updatedTask.getStatusId() == 3;
        if (transitionedToCompleted) {
            log.info("onTaskCompleted");
            userTimerService.onTaskCompleted(updatedTask, user);
        }

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
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + taskId + " not found."));
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
