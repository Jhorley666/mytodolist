package com.bibavix.controller;

import com.bibavix.dto.TaskDTO;
import com.bibavix.exception.TaskNotFoundException;
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
        List<TaskDTO> tasks = Arrays.asList(
                new TaskDTO("test",
                        "test to test",
                        "Low",
                        1, 1,
                        1, 1, "2024-04-02"),
                new TaskDTO("test",
                        "test to test",
                        "High",
                        2, 2,
                        2, 2, "2024-04-02")
        );
        when(taskService.getAllTasksByUser(userDetails.getUsername())).thenReturn(tasks);

        ResponseEntity<List<TaskDTO>> response = taskController.getAllTasks(userDetails);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals(1, response.getBody().get(0).getTaskId());
        verify(taskService, times(1)).getAllTasksByUser(userDetails.getUsername());
    }

    @Test
    void getTaskById_ReturnsTaskIfOwnedByUser() {
        when(taskService.findTaskById(100)).thenReturn(task);
        ResponseEntity<Task> response = taskController.getTaskById(100, userDetails);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(task.getTaskId(), response.getBody().getTaskId());
    }


    void getTaskById_ReturnsForbiddenIfNotOwnedByUser() {

        ResponseEntity<Task> response = taskController.getTaskById(100, userDetails);

        assertEquals(403, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void createTask_SavesTaskForAuthenticatedUser() {

        TaskDTO newTask = new TaskDTO();
        newTask.setTitle("New Task");
        newTask.setDescription("New Description");
        newTask.setStatusId(1);

        when(taskService.createTask(newTask, userDetails.getUsername())).thenReturn(task);

        ResponseEntity<Task> response = taskController.createTask(newTask, userDetails);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(task.getTaskId(), response.getBody().getTaskId());
    }

    @Test
    void updateTask_UpdatesTaskIfOwnedByUser() {

        TaskDTO taskToUpdate = new TaskDTO();
        taskToUpdate.setTaskId(1);
        taskToUpdate.setTitle("Updated Title");
        taskToUpdate.setDescription("Updated Description");
        taskToUpdate.setStatusId(2);

        TaskDTO updatedTask = new TaskDTO();
        updatedTask.setTaskId(1);
        updatedTask.setTitle("Updated Title");
        updatedTask.setDescription("Updated Description");
        updatedTask.setStatusId(2);

        when(taskService.updateTask(1, taskToUpdate, userDetails.getUsername())).thenReturn(updatedTask);


        ResponseEntity<TaskDTO> response = taskController.updateTask(1, taskToUpdate, userDetails);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated Title", response.getBody().getTitle());
        assertEquals("Updated Description", response.getBody().getDescription());
        assertEquals((short) 2, Optional.ofNullable(response.getBody().getStatusId()).get());
    }

    void updateTask_ReturnsForbiddenIfNotOwnedByUser() {
        task.setUserId(2); // Not owned by user
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(taskRepository.findById(100)).thenReturn(Optional.of(task));

        TaskDTO updatedTask = new TaskDTO();
        updatedTask.setTitle("Updated Title");

        ResponseEntity<TaskDTO> response = taskController.updateTask(100, updatedTask, userDetails);

        assertEquals(403, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void deleteTask_DeletesTaskIfOwnedByUser() {

        ResponseEntity<?> response = taskController.deleteTask(100, userDetails);

        assertEquals(200, response.getStatusCodeValue());
        verify(taskService).deleteTask(100, userDetails.getUsername());
    }

    void deleteTask_ReturnsForbiddenIfNotOwnedByUser() {

        ResponseEntity<?> response = taskController.deleteTask(100, userDetails);

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
        when(taskService.findTaskById(100))
                .thenThrow(new TaskNotFoundException(100));
        Exception exception = assertThrows(TaskNotFoundException.class, () -> {
            taskController.getTaskById(100, userDetails);
        });
        assertEquals("Task not found for ID: " + 100, exception.getMessage());
    }
}