package com.bibavix.service;

import com.bibavix.dto.TaskDTO;
import com.bibavix.exception.TaskNotFoundException;
import com.bibavix.model.Task;
import com.bibavix.repository.CategoryRepository;
import com.bibavix.repository.StatusRepository;
import com.bibavix.repository.TaskRepository;
import com.bibavix.service.impl.TaskServiceImpl;
import com.bibavix.util.mapper.TaskMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @InjectMocks
    TaskServiceImpl taskService;
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
        when(taskMapper.toEntity(taskDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(categoryRepository.existsById(taskDTO.getCategoryId())).thenReturn(Boolean.TRUE);
        when(statusRepository.existsById(taskDTO.getStatusId())).thenReturn(Boolean.TRUE);
        Integer taskId = taskService.createTask(taskDTO, 1);
        assertNotNull(taskId);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCategoryDoesNotExist() {
        when(taskDTO.getCategoryId()).thenReturn(1);
        when(categoryRepository.existsById(taskDTO.getCategoryId())).thenReturn(Boolean.FALSE);
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask(taskDTO, 1);
        });
    }

    @Test
    void shouldNotThrowIllegalArgumentExceptionWhenCategoryIdIsNull() {
        when(taskDTO.getCategoryId()).thenReturn(null);
        when(statusRepository.existsById(taskDTO.getStatusId())).thenReturn(Boolean.TRUE);
        when(taskMapper.toEntity(taskDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        assertDoesNotThrow(() -> {taskService.createTask(taskDTO, 1);});
    }

    @Test
    void shouldNotThrowIllegalArgumentExceptionWhenStatusIdIsNull() {
        when(taskDTO.getCategoryId()).thenReturn(1);
        when(categoryRepository.existsById(taskDTO.getCategoryId())).thenReturn(Boolean.TRUE);
        when(taskDTO.getStatusId()).thenReturn(null);
        when(taskMapper.toEntity(taskDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        assertDoesNotThrow(() -> {taskService.createTask(taskDTO, 1);});
    }


    @Test
    void shouldThrowIllegalArgumentExceptionWhenStatusDoesNotExist() {
        when(taskDTO.getCategoryId()).thenReturn(1);
        when(categoryRepository.existsById(taskDTO.getCategoryId())).thenReturn(Boolean.TRUE);
        when(taskDTO.getStatusId()).thenReturn(1);
        when(statusRepository.existsById(taskDTO.getStatusId())).thenReturn(Boolean.FALSE);
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask(taskDTO, 1);
        });
    }

    @Test
    void shouldReturnListOfTasksWhenUserIdIsProvided() {
        List<TaskDTO> taskDTOs = taskService.getAllTasksByUserId(1);
        assertNotNull(taskDTOs);
    }

    @Test
    void shouldReturnTaskDTOWhenUpdateTaskIsCalled() {
        when(taskRepository.findById(1)).thenReturn(java.util.Optional.of(task));
        when(task.getUserId()).thenReturn(1);
        when(taskMapper.toDTO(task)).thenReturn(taskDTO);
        when(categoryRepository.existsById(taskDTO.getCategoryId())).thenReturn(Boolean.TRUE);
        when(statusRepository.existsById(taskDTO.getStatusId())).thenReturn(Boolean.TRUE);
        when(taskRepository.save(task)).thenReturn(task);
        TaskDTO updatedTaskDTO = taskService.updateTask(1, taskDTO, 1);
        assertNotNull(updatedTaskDTO);
    }

    @Test
    void shouldThrowSecurityExceptionWhenUserIdDoesNotMatch() {
        when(taskRepository.findById(1)).thenReturn(java.util.Optional.of(task));
        when(task.getUserId()).thenReturn(2);
        assertThrows(SecurityException.class, () -> {
            taskService.updateTask(1, taskDTO, 1);
        });
    }

    @Test
    void shouldDeleteTaskWhenDeleteTaskIsCalled() {
        Integer taskId = 1;
        when(taskRepository.existsById(taskId)).thenReturn(true);
        taskService.deleteTask(taskId);
        verify(taskRepository).existsById(taskId);
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    void deleteTask_WhenTaskDoesNotExist_ShouldThrowTaskNotFoundException() {
        // Arrange
        Integer taskId = 1;
        when(taskRepository.existsById(taskId)).thenReturn(false);

        // Act & Assert
        assertThrows(TaskNotFoundException.class, () -> {
            taskService.deleteTask(taskId);
        });

        verify(taskRepository).existsById(taskId);
        verify(taskRepository, never()).deleteById(any());
    }

    @Test
    void deleteTask_WhenNullId_ShouldThrowIllegalArgumentException() {
        // Act & Assert
        assertThrows(TaskNotFoundException.class, () -> {
            taskService.deleteTask(null);
        });

        verify(taskRepository, never()).deleteById(any());
    }

}