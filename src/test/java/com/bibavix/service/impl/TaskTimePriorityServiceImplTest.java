package com.bibavix.service.impl;

import com.bibavix.dto.TaskTimePriorityDTO;
import com.bibavix.exception.ResourceNotFoundException;
import com.bibavix.model.TaskTimePriority;
import com.bibavix.repository.TaskTimePriorityRepository;
import com.bibavix.util.mapper.TaskTimePriorityMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskTimePriorityServiceImplTest {

    @InjectMocks
    TaskTimePriorityServiceImpl taskTimePriorityService;

    @Mock
    TaskTimePriorityRepository taskTimePriorityRepository;

    @Mock
    TaskTimePriorityMapper taskTimePriorityMapper;

    private TaskTimePriority taskTimePriority;
    private TaskTimePriorityDTO taskTimePriorityDTO;

    @BeforeEach
    void setUp() {
        Date testDate = new Date();
        
        taskTimePriority = new TaskTimePriority();
        taskTimePriority.setTaskTimePriorityId(1);
        taskTimePriority.setTime(testDate);
        taskTimePriority.setPriorityId(10);

        taskTimePriorityDTO = new TaskTimePriorityDTO();
        taskTimePriorityDTO.setTaskTimePriorityId(1);
        taskTimePriorityDTO.setTime(testDate);
        taskTimePriorityDTO.setPriorityId(10);
    }

    @Test
    void shouldReturnTaskTimePriorityDTOWhenCreateTaskTimePriority() {
        when(taskTimePriorityMapper.toEntity(taskTimePriorityDTO)).thenReturn(taskTimePriority);
        when(taskTimePriorityRepository.save(taskTimePriority)).thenReturn(taskTimePriority);
        when(taskTimePriorityMapper.toDTO(taskTimePriority)).thenReturn(taskTimePriorityDTO);

        TaskTimePriorityDTO createdTaskTimePriority = taskTimePriorityService.createTaskTimePriority(taskTimePriorityDTO);

        Assertions.assertNotNull(createdTaskTimePriority);
        Assertions.assertEquals(10, createdTaskTimePriority.getPriorityId());
        verify(taskTimePriorityMapper, times(1)).toEntity(any());
        verify(taskTimePriorityRepository, times(1)).save(any());
        verify(taskTimePriorityMapper, times(1)).toDTO(any());
    }

    @Test
    void shouldReturnTaskTimePriorityListWhenGetAllTaskTimePriorities() {
        TaskTimePriority taskTimePriority2 = new TaskTimePriority();
        taskTimePriority2.setTaskTimePriorityId(2);
        taskTimePriority2.setTime(new Date());
        taskTimePriority2.setPriorityId(20);

        TaskTimePriorityDTO taskTimePriorityDTO2 = new TaskTimePriorityDTO();
        taskTimePriorityDTO2.setTaskTimePriorityId(2);
        taskTimePriorityDTO2.setTime(new Date());
        taskTimePriorityDTO2.setPriorityId(20);

        List<TaskTimePriority> taskTimePriorityList = List.of(taskTimePriority, taskTimePriority2);
        when(taskTimePriorityRepository.findAll()).thenReturn(taskTimePriorityList);
        when(taskTimePriorityMapper.toDTO(taskTimePriority)).thenReturn(taskTimePriorityDTO);
        when(taskTimePriorityMapper.toDTO(taskTimePriority2)).thenReturn(taskTimePriorityDTO2);

        List<TaskTimePriorityDTO> taskTimePriorityDTOS = taskTimePriorityService.getAllTaskTimePriorities();

        Assertions.assertNotNull(taskTimePriorityDTOS);
        Assertions.assertFalse(taskTimePriorityDTOS.isEmpty());
        Assertions.assertEquals(2, taskTimePriorityDTOS.size());
        verify(taskTimePriorityRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnTaskTimePriorityWhenGetTaskTimePriorityById() {
        when(taskTimePriorityRepository.findById(1)).thenReturn(Optional.of(taskTimePriority));
        when(taskTimePriorityMapper.toDTO(taskTimePriority)).thenReturn(taskTimePriorityDTO);

        TaskTimePriorityDTO foundTaskTimePriority = taskTimePriorityService.getTaskTimePriorityById(1);

        Assertions.assertNotNull(foundTaskTimePriority);
        Assertions.assertEquals(10, foundTaskTimePriority.getPriorityId());
        verify(taskTimePriorityRepository, times(1)).findById(1);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenGetTaskTimePriorityById() {
        when(taskTimePriorityRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                taskTimePriorityService.getTaskTimePriorityById(1));
    }

    @Test
    void shouldReturnTaskTimePriorityWhenGetTaskTimePriorityByPriorityId() {
        when(taskTimePriorityRepository.findByPriorityId(10)).thenReturn(taskTimePriority);
        when(taskTimePriorityMapper.toDTO(taskTimePriority)).thenReturn(taskTimePriorityDTO);

        TaskTimePriorityDTO foundTaskTimePriority = taskTimePriorityService.getTaskTimePriorityByPriorityId(10);

        Assertions.assertNotNull(foundTaskTimePriority);
        Assertions.assertEquals(10, foundTaskTimePriority.getPriorityId());
        verify(taskTimePriorityRepository, times(1)).findByPriorityId(10);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenGetTaskTimePriorityByPriorityId() {
        when(taskTimePriorityRepository.findByPriorityId(10)).thenReturn(null);

        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                taskTimePriorityService.getTaskTimePriorityByPriorityId(10));
    }

    @Test
    void shouldUpdateTaskTimePriorityWhenUpdateTaskTimePriority() {
        TaskTimePriorityDTO updatedDTO = new TaskTimePriorityDTO();
        updatedDTO.setTaskTimePriorityId(1);
        updatedDTO.setTime(new Date());
        updatedDTO.setPriorityId(15);

        when(taskTimePriorityRepository.findById(1)).thenReturn(Optional.of(taskTimePriority));
        when(taskTimePriorityRepository.save(taskTimePriority)).thenReturn(taskTimePriority);
        when(taskTimePriorityMapper.toDTO(taskTimePriority)).thenReturn(updatedDTO);

        TaskTimePriorityDTO result = taskTimePriorityService.updateTaskTimePriority(updatedDTO);

        Assertions.assertNotNull(result);
        verify(taskTimePriorityMapper).updateTaskTimePriorityFromDTO(updatedDTO, taskTimePriority);
        verify(taskTimePriorityRepository).save(taskTimePriority);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdateTaskTimePriorityNotFound() {
        when(taskTimePriorityRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                taskTimePriorityService.updateTaskTimePriority(taskTimePriorityDTO));
    }

    @Test
    void verifyDeleteTaskTimePriority() {
        when(taskTimePriorityRepository.findById(1)).thenReturn(Optional.of(taskTimePriority));
        when(taskTimePriorityMapper.toDTO(taskTimePriority)).thenReturn(taskTimePriorityDTO);

        taskTimePriorityService.deleteTaskTimePriorityById(1);

        verify(taskTimePriorityRepository, times(1)).deleteById(1);
        verify(taskTimePriorityRepository, times(1)).findById(1);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenDeleteTaskTimePriorityNotFound() {
        when(taskTimePriorityRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                taskTimePriorityService.deleteTaskTimePriorityById(1));
    }
}
