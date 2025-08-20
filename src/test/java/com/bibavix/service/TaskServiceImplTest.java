package com.bibavix.service;

import com.bibavix.dto.TaskDTO;
import com.bibavix.exception.TaskNotFoundException;
import com.bibavix.model.Task;
import com.bibavix.model.User;
import com.bibavix.repository.CategoryRepository;
import com.bibavix.repository.StatusRepository;
import com.bibavix.repository.TaskRepository;
import com.bibavix.service.impl.TaskServiceImpl;
import com.bibavix.service.impl.UserDetailsServiceImpl;
import com.bibavix.util.mapper.TaskMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @InjectMocks
    TaskServiceImpl taskService;
    @Mock
    UserDetailsServiceImpl userDetailsService;
    @Mock
    TaskRepository taskRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    StatusRepository statusRepository;
    @Mock
    TaskMapper taskMapper;
    @Mock
    TaskDTO taskDTO;
    @Mock
    Task task;
    @Mock
    List<Task> tasks;

    @Test
    void shouldReturnLongWhenTaskIsCreated() {
        User user = new User();
        user.setUserId(1);
        when(userDetailsService.findUserByUsername("root")).thenReturn(user);
        when(taskMapper.toEntity(taskDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(categoryRepository.existsById(taskDTO.getCategoryId())).thenReturn(Boolean.TRUE);
        when(statusRepository.existsById(taskDTO.getStatusId())).thenReturn(Boolean.TRUE);

        Task taskId = taskService.createTask(taskDTO, "root");

        assertNotNull(taskId);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCategoryDoesNotExist() {
        when(taskDTO.getCategoryId()).thenReturn(1);
        when(categoryRepository.existsById(taskDTO.getCategoryId())).thenReturn(Boolean.FALSE);
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask(taskDTO, "root");
        });
    }

    @Test
    void shouldNotThrowIllegalArgumentExceptionWhenCategoryIdIsNull() {
        User user = new User();
        user.setUserId(1);
        when(userDetailsService.findUserByUsername("root")).thenReturn(user);
        when(taskDTO.getCategoryId()).thenReturn(null);
        when(statusRepository.existsById(taskDTO.getStatusId())).thenReturn(Boolean.TRUE);
        when(taskMapper.toEntity(taskDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        assertDoesNotThrow(() -> {taskService.createTask(taskDTO, "root");});
    }

    @Test
    void shouldNotThrowIllegalArgumentExceptionWhenStatusIdIsNull() {
        User user = new User();
        user.setUserId(1);
        when(userDetailsService.findUserByUsername("root")).thenReturn(user);
        when(taskDTO.getCategoryId()).thenReturn(1);
        when(categoryRepository.existsById(taskDTO.getCategoryId())).thenReturn(Boolean.TRUE);
        when(taskDTO.getStatusId()).thenReturn(null);
        when(taskMapper.toEntity(taskDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        assertDoesNotThrow(() -> {taskService.createTask(taskDTO, "root");});
    }


    @Test
    void shouldThrowIllegalArgumentExceptionWhenStatusDoesNotExist() {
        when(taskDTO.getCategoryId()).thenReturn(1);
        when(categoryRepository.existsById(taskDTO.getCategoryId())).thenReturn(Boolean.TRUE);
        when(taskDTO.getStatusId()).thenReturn(1);
        when(statusRepository.existsById(taskDTO.getStatusId())).thenReturn(Boolean.FALSE);
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask(taskDTO, "root");
        });
    }

    @Test
    void shouldReturnListOfTasksWhenUserIdIsProvided() {
        User user = new User();
        user.setUserId(1);
        when(userDetailsService.findUserByUsername("root")).thenReturn(user);
        List<TaskDTO> taskDTOs = taskService.getAllTasksByUser("root");
        assertNotNull(taskDTOs);
    }

    @Test
    void shouldReturnTaskDTOWhenUpdateTaskIsCalled() {
        User user = new User();
        user.setUserId(1);
        when(userDetailsService.findUserByUsername("root")).thenReturn(user);
        when(taskRepository.findById(1)).thenReturn(java.util.Optional.of(task));
        when(task.getUserId()).thenReturn(1);
        when(taskMapper.toDTO(task)).thenReturn(taskDTO);
        when(categoryRepository.existsById(taskDTO.getCategoryId())).thenReturn(Boolean.TRUE);
        when(statusRepository.existsById(taskDTO.getStatusId())).thenReturn(Boolean.TRUE);
        when(taskRepository.save(task)).thenReturn(task);
        TaskDTO updatedTaskDTO = taskService.updateTask(1, taskDTO, "root");
        assertNotNull(updatedTaskDTO);
    }

    @Test
    void shouldThrowSecurityExceptionWhenUserIdDoesNotMatch() {
        User user = new User();
        user.setUserId(1);
        when(userDetailsService.findUserByUsername("root")).thenReturn(user);
        when(taskRepository.findById(1)).thenReturn(java.util.Optional.of(task));
        when(task.getUserId()).thenReturn(2);
        assertThrows(SecurityException.class, () -> {
            taskService.updateTask(1, taskDTO, "root");
        });
    }

    @Test
    void shouldDeleteTaskWhenDeleteTaskIsCalled() {
        User user = new User();
        user.setUserId(1);
        Task task = new Task();
        task.setTaskId(1);
        task.setUserId(1);
        Integer taskId = 1;
        when(userDetailsService.findUserByUsername("root")).thenReturn(user);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        taskService.deleteTask(taskId, "root");
        verify(taskRepository).findById(taskId);
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    void deleteTask_WhenTaskDoesNotExist_ShouldThrowTaskNotFoundException() {
        // Arrange
        Integer taskId = 1;
        // Act & Assert
        assertThrows(TaskNotFoundException.class, () -> {
            taskService.deleteTask(taskId, "root");
        });

        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).deleteById(any());
    }

    @Test
    void deleteTask_WhenNullId_ShouldThrowIllegalArgumentException() {
        // Act & Assert
        assertThrows(TaskNotFoundException.class, () -> {
            taskService.deleteTask(null, null);
        });

        verify(taskRepository, never()).deleteById(any());
    }

}