package com.bibavix.controller;

import com.bibavix.dto.PriorityDTO;
import com.bibavix.service.PriorityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriorityControllerTest {
    @InjectMocks
    PriorityController priorityController;

    @Mock
    PriorityService priorityService;

    @Mock
    PriorityDTO priorityDTO;

    @Test
    void shouldReturnNotNullPriorityWhenFindById() {
        //Arrange
        int id = 1;

        //Act
        when(priorityService.getPriorityById(id)).thenReturn(priorityDTO);
        ResponseEntity<PriorityDTO> response = priorityController.getPriorityById(id);

        //Asserts
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(priorityDTO, response.getBody());
    }

    @Test
    void shouldReturnNotNullPriorityWhenCreatePriority() {
        //Arrange
        PriorityDTO inputDTO = new PriorityDTO();
        inputDTO.setPriorityName("High");

        //Act
        when(priorityService.createPriority(inputDTO)).thenReturn(priorityDTO);
        ResponseEntity<PriorityDTO> response = priorityController.createPriority(inputDTO);

        //Asserts
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(priorityDTO, response.getBody());
    }

    @Test
    void shouldReturnNotNullListWhenGetAllPriorities() {
        //Arrange
        List<PriorityDTO> priorityList = List.of(priorityDTO);

        //Act
        when(priorityService.getAllPriorities()).thenReturn(priorityList);
        ResponseEntity<List<PriorityDTO>> response = priorityController.getAllPriorities();

        //Asserts
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnNotNullPriorityWhenUpdatePriority() {
        //Arrange
        int id = 1;
        PriorityDTO inputDTO = new PriorityDTO();
        inputDTO.setPriorityName("Medium");

        //Act
        when(priorityService.updatePriority(any(PriorityDTO.class))).thenReturn(priorityDTO);
        ResponseEntity<PriorityDTO> response = priorityController.updatePriority(id, inputDTO);

        //Asserts
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(priorityDTO, response.getBody());
    }

    @Test
    void shouldReturnOkWhenDeletePriority() {
        //Arrange
        int id = 1;

        //Act
        ResponseEntity<?> response = priorityController.deletePriority(id);

        //Asserts
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}

