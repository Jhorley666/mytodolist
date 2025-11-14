package com.bibavix.controller;

import com.bibavix.dto.TaskTimePriorityDTO;
import com.bibavix.service.TaskTimePriorityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

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

    @Test
    void shouldReturnNotNullTaskTimePriorityWhenFindById() {
        //Arrange
        int id = 1;

        //Act
        when(taskTimePriorityService.getTaskTimePriorityById(id)).thenReturn(taskTimePriorityDTO);
        ResponseEntity<TaskTimePriorityDTO> response = taskTimePriorityController.getTaskTimePriorityById(id);

        //Asserts
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(taskTimePriorityDTO, response.getBody());
    }

    @Test
    void shouldReturnNotNullTaskTimePriorityWhenCreateTaskTimePriority() {
        //Arrange
        TaskTimePriorityDTO inputDTO = new TaskTimePriorityDTO();
        inputDTO.setTime(new Date());
        inputDTO.setPriorityId(10);

        //Act
        when(taskTimePriorityService.createTaskTimePriority(inputDTO)).thenReturn(taskTimePriorityDTO);
        ResponseEntity<TaskTimePriorityDTO> response = taskTimePriorityController.createTaskTimePriority(inputDTO);

        //Asserts
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(taskTimePriorityDTO, response.getBody());
    }

    @Test
    void shouldReturnNotNullListWhenGetAllTaskTimePriorities() {
        //Arrange
        List<TaskTimePriorityDTO> taskTimePriorityList = List.of(taskTimePriorityDTO);

        //Act
        when(taskTimePriorityService.getAllTaskTimePriorities()).thenReturn(taskTimePriorityList);
        ResponseEntity<List<TaskTimePriorityDTO>> response = taskTimePriorityController.getAllTaskTimePriorities();

        //Asserts
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnNotNullTaskTimePriorityWhenGetByPriorityId() {
        //Arrange
        int priorityId = 10;

        //Act
        when(taskTimePriorityService.getTaskTimePriorityByPriorityId(priorityId)).thenReturn(taskTimePriorityDTO);
        ResponseEntity<TaskTimePriorityDTO> response = taskTimePriorityController.getTaskTimePriorityByPriorityId(priorityId);

        //Asserts
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(taskTimePriorityDTO, response.getBody());
    }

    @Test
    void shouldReturnNotNullTaskTimePriorityWhenUpdateTaskTimePriority() {
        //Arrange
        int id = 1;
        TaskTimePriorityDTO inputDTO = new TaskTimePriorityDTO();
        inputDTO.setTime(new Date());
        inputDTO.setPriorityId(15);

        //Act
        when(taskTimePriorityService.updateTaskTimePriority(any(TaskTimePriorityDTO.class))).thenReturn(taskTimePriorityDTO);
        ResponseEntity<TaskTimePriorityDTO> response = taskTimePriorityController.updateTaskTimePriority(id, inputDTO);

        //Asserts
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(taskTimePriorityDTO, response.getBody());
    }

    @Test
    void shouldReturnOkWhenDeleteTaskTimePriority() {
        //Arrange
        int id = 1;

        //Act
        ResponseEntity<?> response = taskTimePriorityController.deleteTaskTimePriority(id);

        //Asserts
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}

