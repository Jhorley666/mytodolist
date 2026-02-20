package com.bibavix.controller;

import com.bibavix.dto.TaskTimePriorityDTO;
import com.bibavix.model.User;
import com.bibavix.service.TaskTimePriorityService;
import com.bibavix.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskTimePriorityControllerTest {
    @InjectMocks
    TaskTimePriorityController taskTimePriorityController;

    @Mock
    TaskTimePriorityService taskTimePriorityService;

    @Mock
    TaskTimePriorityDTO taskTimePriorityDTO;

    @Mock
    UserDetails userDetails;

    @Mock
    UserDetailsServiceImpl userDetailsService;

    @Mock
    User user;

    @Test
    void shouldReturnNotNullTaskTimePriorityWhenFindById() {
        // Arrange
        UUID id = UUID.randomUUID();
        String username = "test";
        // Act
        when(taskTimePriorityService.getTaskTimePriorityById(id, username)).thenReturn(taskTimePriorityDTO);
        when(userDetails.getUsername()).thenReturn(username);
        ResponseEntity<TaskTimePriorityDTO> response = taskTimePriorityController.getTaskTimePriorityById(id,
                userDetails);

        // Asserts
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(taskTimePriorityDTO, response.getBody());
    }

    @Test
    void shouldReturnNotNullTaskTimePriorityWhenCreateTaskTimePriority() {
        // Arrange
        TaskTimePriorityDTO inputDTO = new TaskTimePriorityDTO();
        inputDTO.setTime(1000L);
        inputDTO.setPriorityId(UUID.randomUUID());

        // Act
        when(taskTimePriorityService.createTaskTimePriority(inputDTO, "username")).thenReturn(taskTimePriorityDTO);
        when(userDetails.getUsername()).thenReturn("username");
        ResponseEntity<TaskTimePriorityDTO> response = taskTimePriorityController.createTaskTimePriority(inputDTO,
                userDetails);

        // Asserts
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(taskTimePriorityDTO, response.getBody());
    }

    @Test
    void shouldReturnNotNullListWhenGetAllTaskTimePriorities() {
        // Arrange
        List<TaskTimePriorityDTO> taskTimePriorityList = List.of(taskTimePriorityDTO);

        // Act
        when(taskTimePriorityService.getAllTaskTimePriorities()).thenReturn(taskTimePriorityList);
        ResponseEntity<List<TaskTimePriorityDTO>> response = taskTimePriorityController.getAllTaskTimePriorities();

        // Asserts
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnNotNullTaskTimePriorityWhenGetByPriorityId() {
        // Arrange
        UUID priorityId = UUID.randomUUID();
        String username = "test";
        // Act
        when(taskTimePriorityService.getTaskTimePriorityByPriorityId(priorityId, username))
                .thenReturn(taskTimePriorityDTO);
        when(userDetails.getUsername()).thenReturn(username);
        ResponseEntity<TaskTimePriorityDTO> response = taskTimePriorityController
                .getTaskTimePriorityByPriorityId(priorityId, userDetails);

        // Asserts
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(taskTimePriorityDTO, response.getBody());
    }

    @Test
    void shouldReturnNotNullTaskTimePriorityWhenUpdateTaskTimePriority() {
        // Arrange
        UUID id = UUID.randomUUID();
        String username = "test";
        TaskTimePriorityDTO inputDTO = new TaskTimePriorityDTO();
        inputDTO.setTime(1000L);
        inputDTO.setPriorityId(UUID.randomUUID());

        // Act
        when(userDetails.getUsername()).thenReturn("test");
        when(taskTimePriorityService.updateTaskTimePriority(inputDTO, "test"))
                .thenReturn(taskTimePriorityDTO);
        ResponseEntity<TaskTimePriorityDTO> response = taskTimePriorityController.updateTaskTimePriority(id, inputDTO,
                userDetails);

        // Asserts
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(taskTimePriorityDTO, response.getBody());
    }

    @Test
    void shouldReturnOkWhenDeleteTaskTimePriority() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        ResponseEntity<?> response = taskTimePriorityController.deleteTaskTimePriority(id, userDetails);

        // Asserts
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldReturnNotNullListWhenGetTaskTimePrioritiesByUserId() {
        // Arrange
        String username = "usertest";
        List<TaskTimePriorityDTO> taskTimePriorityList = List.of(taskTimePriorityDTO);
        when(userDetails.getUsername()).thenReturn(username);
        // Act
        when(taskTimePriorityService.getTaskTimePrioritiesByUser(username)).thenReturn(taskTimePriorityList);
        ResponseEntity<List<TaskTimePriorityDTO>> response = taskTimePriorityController
                .getTaskTimePrioritiesByUser(userDetails);

        // Asserts
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }
}
