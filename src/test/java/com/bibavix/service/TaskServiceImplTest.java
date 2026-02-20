package com.bibavix.service;

import com.bibavix.dto.TaskDTO;
import com.bibavix.exception.ResourceNotFoundException;
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
import java.util.UUID;

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

    private final UUID userId1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final UUID userId2 = UUID.fromString("00000000-0000-0000-0000-000000000099");
    private final UUID categoryId1 = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private final UUID statusId1 = UUID.fromString("00000000-0000-0000-0000-000000000003");
    private final UUID taskId1 = UUID.fromString("00000000-0000-0000-0000-000000000004");

    @Test
    void shouldReturnLongWhenTaskIsCreated() {
        User user = new User();
        user.setUserId(userId1);
        when(userDetailsService.findUserByUsername("root")).thenReturn(user);
        when(taskMapper.toEntity(taskDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);

        Task createdTask = taskService.createTask(taskDTO, "root");

        assertNotNull(createdTask);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCategoryDoesNotExist() {
        when(taskDTO.getCategoryId()).thenReturn(categoryId1);
        when(categoryRepository.existsById(taskDTO.getCategoryId())).thenReturn(Boolean.FALSE);
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask(taskDTO, "root");
        });
    }

    @Test
    void shouldNotThrowIllegalArgumentExceptionWhenCategoryIdIsNull() {
        User user = new User();
        user.setUserId(userId1);
        when(userDetailsService.findUserByUsername("root")).thenReturn(user);
        when(taskDTO.getCategoryId()).thenReturn(null);
        when(taskMapper.toEntity(taskDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        assertDoesNotThrow(() -> {
            taskService.createTask(taskDTO, "root");
        });
    }

    @Test
    void shouldNotThrowIllegalArgumentExceptionWhenStatusIdIsNull() {
        User user = new User();
        user.setUserId(userId1);
        when(userDetailsService.findUserByUsername("root")).thenReturn(user);
        when(taskDTO.getCategoryId()).thenReturn(categoryId1);
        when(categoryRepository.existsById(taskDTO.getCategoryId())).thenReturn(Boolean.TRUE);
        when(taskDTO.getStatusId()).thenReturn(null);
        when(taskMapper.toEntity(taskDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        assertDoesNotThrow(() -> {
            taskService.createTask(taskDTO, "root");
        });
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenStatusDoesNotExist() {
        when(taskDTO.getCategoryId()).thenReturn(categoryId1);
        when(categoryRepository.existsById(taskDTO.getCategoryId())).thenReturn(Boolean.TRUE);
        when(taskDTO.getStatusId()).thenReturn(statusId1);
        when(statusRepository.existsById(taskDTO.getStatusId())).thenReturn(Boolean.FALSE);
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask(taskDTO, "root");
        });
    }

    @Test
    void shouldReturnListOfTasksWhenUserIdIsProvided() {
        User user = new User();
        user.setUserId(userId1);
        when(userDetailsService.findUserByUsername("root")).thenReturn(user);
        List<TaskDTO> taskDTOs = taskService.getAllTasksByUser("root");
        assertNotNull(taskDTOs);
    }

    @Test
    void shouldReturnTaskDTOWhenUpdateTaskIsCalled() {
        User user = new User();
        user.setUserId(userId1);
        when(userDetailsService.findUserByUsername("root")).thenReturn(user);
        when(taskRepository.findById(taskId1)).thenReturn(java.util.Optional.of(task));
        when(task.getUserId()).thenReturn(userId1);
        when(taskMapper.toDTO(task)).thenReturn(taskDTO);
        when(taskRepository.save(task)).thenReturn(task);
        TaskDTO updatedTaskDTO = taskService.updateTask(taskId1, taskDTO, "root");
        assertNotNull(updatedTaskDTO);
    }

    @Test
    void shouldThrowSecurityExceptionWhenUserIdDoesNotMatch() {
        User user = new User();
        user.setUserId(userId1);
        when(userDetailsService.findUserByUsername("root")).thenReturn(user);
        when(taskRepository.findById(taskId1)).thenReturn(java.util.Optional.of(task));
        when(task.getUserId()).thenReturn(userId2);
        assertThrows(SecurityException.class, () -> {
            taskService.updateTask(taskId1, taskDTO, "root");
        });
    }

    @Test
    void shouldDeleteTaskWhenDeleteTaskIsCalled() {
        User user = new User();
        user.setUserId(userId1);
        Task task = new Task();
        task.setTaskId(taskId1);
        task.setUserId(userId1);
        UUID taskId = taskId1;
        when(userDetailsService.findUserByUsername("root")).thenReturn(user);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        taskService.deleteTask(taskId, "root");
        verify(taskRepository).findById(taskId);
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    void deleteTask_WhenTaskDoesNotExist_ShouldThrowTaskNotFoundException() {
        // Arrange
        UUID taskId = taskId1;
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.deleteTask(taskId, "root");
        });

        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).deleteById(any());
    }

    @Test
    void deleteTask_WhenNullId_ShouldThrowIllegalArgumentException() {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.deleteTask(null, null);
        });

        verify(taskRepository, never()).deleteById(any());
    }

}