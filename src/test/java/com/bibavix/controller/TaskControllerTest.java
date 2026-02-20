package com.bibavix.controller;

import com.bibavix.dto.TaskDTO;
import com.bibavix.exception.ResourceNotFoundException;
import com.bibavix.model.Task;
import com.bibavix.model.User;
import com.bibavix.repository.TaskRepository;
import com.bibavix.repository.UserRepository;
import com.bibavix.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

class TaskControllerTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    TaskService taskService;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private TaskController taskController;

    private User user;
    private Task task;
    private UUID userId;
    private UUID taskId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        taskId = UUID.randomUUID();

        user = new User();
        user.setUserId(userId);
        user.setUsername("testuser");

        task = new Task();
        task.setTaskId(taskId);
        task.setUserId(userId);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatusId(UUID.randomUUID());
    }

    @Test
    void getAllTasks_ReturnsTasksForAuthenticatedUser() {
        UUID tId1 = UUID.randomUUID();
        UUID tId2 = UUID.randomUUID();
        List<TaskDTO> tasks = Arrays.asList(
                new TaskDTO("test",
                        "test to test",
                        UUID.randomUUID(), tId1,
                        UUID.randomUUID(), UUID.randomUUID(),
                        UUID.randomUUID(), "2024-04-02", "2024-04-02", "2024-04-02"),
                new TaskDTO("test",
                        "test to test",
                        UUID.randomUUID(), tId2,
                        UUID.randomUUID(), UUID.randomUUID(),
                        UUID.randomUUID(), "2024-04-02", "2024-04-02", "2024-04-02"));
        when(taskService.getAllTasksByUser(userDetails.getUsername())).thenReturn(tasks);

        ResponseEntity<List<TaskDTO>> response = taskController.getAllTasks(userDetails);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals(tId1, response.getBody().get(0).getTaskId());
        verify(taskService, times(1)).getAllTasksByUser(userDetails.getUsername());
    }

    @Test
    void getTaskById_ReturnsTaskIfOwnedByUser() {
        when(taskService.findTaskById(taskId)).thenReturn(task);
        ResponseEntity<Task> response = taskController.getTaskById(taskId, userDetails);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(task.getTaskId(), response.getBody().getTaskId());
    }

    void getTaskById_ReturnsForbiddenIfNotOwnedByUser() {

        ResponseEntity<Task> response = taskController.getTaskById(taskId, userDetails);

        assertEquals(403, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void createTask_SavesTaskForAuthenticatedUser() {

        TaskDTO newTask = new TaskDTO();
        newTask.setTitle("New Task");
        newTask.setDescription("New Description");
        newTask.setStatusId(UUID.randomUUID());

        when(taskService.createTask(newTask, userDetails.getUsername())).thenReturn(task);

        ResponseEntity<Task> response = taskController.createTask(newTask, userDetails);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(task.getTaskId(), response.getBody().getTaskId());
    }

    @Test
    void updateTask_UpdatesTaskIfOwnedByUser() {

        TaskDTO taskToUpdate = new TaskDTO();
        taskToUpdate.setTaskId(taskId);
        taskToUpdate.setTitle("Updated Title");
        taskToUpdate.setDescription("Updated Description");
        taskToUpdate.setStatusId(UUID.randomUUID());

        TaskDTO updatedTask = new TaskDTO();
        updatedTask.setTaskId(taskId);
        updatedTask.setTitle("Updated Title");
        updatedTask.setDescription("Updated Description");
        updatedTask.setStatusId(taskToUpdate.getStatusId());

        when(taskService.updateTask(taskId, taskToUpdate, userDetails.getUsername())).thenReturn(updatedTask);

        ResponseEntity<TaskDTO> response = taskController.updateTask(taskId, taskToUpdate, userDetails);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated Title", response.getBody().getTitle());
        assertEquals("Updated Description", response.getBody().getDescription());
        assertEquals(taskToUpdate.getStatusId(), response.getBody().getStatusId());
    }

    void updateTask_ReturnsForbiddenIfNotOwnedByUser() {
        task.setUserId(UUID.randomUUID()); // Not owned by user
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        TaskDTO updatedTask = new TaskDTO();
        updatedTask.setTitle("Updated Title");

        ResponseEntity<TaskDTO> response = taskController.updateTask(taskId, updatedTask, userDetails);

        assertEquals(403, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void deleteTask_DeletesTaskIfOwnedByUser() {

        ResponseEntity<?> response = taskController.deleteTask(taskId, userDetails);

        assertEquals(200, response.getStatusCodeValue());
        verify(taskService).deleteTask(taskId, userDetails.getUsername());
    }

    void deleteTask_ReturnsForbiddenIfNotOwnedByUser() {

        ResponseEntity<?> response = taskController.deleteTask(taskId, userDetails);

        assertEquals(403, response.getStatusCodeValue());
        verify(taskRepository, never()).delete(task);
    }

    @Test
    void getAllTasks_ReturnsUserNotFound() {
        when(taskService.getAllTasksByUser(userDetails.getUsername()))
                .thenThrow(new UsernameNotFoundException(TaskController.USER_NOT_FOUND));
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            taskController.getAllTasks(userDetails);
        });
        assertEquals(TaskController.USER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void getTaskById_ReturnsTaskNotFound() {
        when(taskService.findTaskById(taskId))
                .thenThrow(new ResourceNotFoundException("Task with id 100 not found"));
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            taskController.getTaskById(taskId, userDetails);
        });
        assertEquals("Task with id 100 not found", exception.getMessage());
    }
}