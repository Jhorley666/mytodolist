package com.bibavix.controller;

import com.bibavix.model.Task;
import com.bibavix.model.User;
import com.bibavix.repository.TaskRepository;
import com.bibavix.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private TaskController taskController;

    private User user;
    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUserId(1);
        user.setUsername("testuser");

        task = new Task();
        task.setTaskId(100);
        task.setUserId(1);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatusId((short) 1);
    }

    @Test
    void getAllTasks_ReturnsTasksForAuthenticatedUser() {
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(taskRepository.findAllByUserId(1)).thenReturn(List.of(task));

        ResponseEntity<List<Task>> response = taskController.getAllTasks(userDetails);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(task.getTaskId(), response.getBody().get(0).getTaskId());
    }

    @Test
    void getTaskById_ReturnsTaskIfOwnedByUser() {
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(taskRepository.findById(100)).thenReturn(Optional.of(task));

        ResponseEntity<Task> response = taskController.getTaskById(100, userDetails);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(task.getTaskId(), response.getBody().getTaskId());
    }

    @Test
    void getTaskById_ReturnsForbiddenIfNotOwnedByUser() {
        task.setUserId(2); // Not owned by user
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(taskRepository.findById(100)).thenReturn(Optional.of(task));

        ResponseEntity<Task> response = taskController.getTaskById(100, userDetails);

        assertEquals(403, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void createTask_SavesTaskForAuthenticatedUser() {
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(taskRepository.save(ArgumentMatchers.any(Task.class))).thenReturn(task);

        Task newTask = new Task();
        newTask.setTitle("New Task");
        newTask.setDescription("New Description");
        newTask.setStatusId((short) 1);

        ResponseEntity<Task> response = taskController.createTask(newTask, userDetails);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(task.getTaskId(), response.getBody().getTaskId());
        verify(taskRepository).save(ArgumentMatchers.any(Task.class));
    }

    @Test
    void updateTask_UpdatesTaskIfOwnedByUser() {
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(taskRepository.findById(100)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Title");
        updatedTask.setDescription("Updated Description");
        updatedTask.setStatusId((short) 2);

        ResponseEntity<Task> response = taskController.updateTask(100, updatedTask, userDetails);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated Title", response.getBody().getTitle());
        assertEquals("Updated Description", response.getBody().getDescription());
        assertEquals((short) 2, Optional.ofNullable(response.getBody().getStatusId()).get());
    }

    @Test
    void updateTask_ReturnsForbiddenIfNotOwnedByUser() {
        task.setUserId(2); // Not owned by user
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(taskRepository.findById(100)).thenReturn(Optional.of(task));

        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Title");

        ResponseEntity<Task> response = taskController.updateTask(100, updatedTask, userDetails);

        assertEquals(403, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void deleteTask_DeletesTaskIfOwnedByUser() {
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(taskRepository.findById(100)).thenReturn(Optional.of(task));

        ResponseEntity<?> response = taskController.deleteTask(100, userDetails);

        assertEquals(200, response.getStatusCodeValue());
        verify(taskRepository).delete(task);
    }

    @Test
    void deleteTask_ReturnsForbiddenIfNotOwnedByUser() {
        task.setUserId(2); // Not owned by user
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(taskRepository.findById(100)).thenReturn(Optional.of(task));

        ResponseEntity<?> response = taskController.deleteTask(100, userDetails);

        assertEquals(403, response.getStatusCodeValue());
        verify(taskRepository, never()).delete(task);
    }

    @Test
    void getAllTasks_ReturnsUserNotFound() {
        when(userDetails.getUsername()).thenReturn("unknown");
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskController.getAllTasks(userDetails);
        });
        assertEquals(TaskController.USER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void getTaskById_ReturnsTaskNotFound() {
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(taskRepository.findById(100)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskController.getTaskById(100, userDetails);
        });
        assertEquals(TaskController.TASK_NOT_FOUND, exception.getMessage());
    }
}