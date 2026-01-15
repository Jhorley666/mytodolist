package com.bibavix.service.impl;

import com.bibavix.dto.TaskTimePriorityDTO;
import com.bibavix.exception.ResourceNotFoundException;
import com.bibavix.model.TaskTimePriority;
import com.bibavix.model.User;
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
    @Mock
    UserDetailsServiceImpl userDetailsService;
    @Mock
    User user;
    private TaskTimePriority taskTimePriority;
    private TaskTimePriorityDTO taskTimePriorityDTO;

    @BeforeEach
    void setUp() {
        Integer testDate = 1000;

        taskTimePriority = new TaskTimePriority();
        taskTimePriority.setTaskTimePriorityId(1);
        taskTimePriority.setTime(testDate);
        taskTimePriority.setPriorityId(10);
        taskTimePriority.setUserId(100);

        taskTimePriorityDTO = new TaskTimePriorityDTO();
        taskTimePriorityDTO.setTaskTimePriorityId(1);
        taskTimePriorityDTO.setTime(1000L);
        taskTimePriorityDTO.setPriorityId(10);
        taskTimePriorityDTO.setUserId(100);
    }

    @Test
    void shouldReturnTaskTimePriorityDTOWhenCreateTaskTimePriority() {
        String username = "username";

        when(taskTimePriorityMapper.toEntity(taskTimePriorityDTO)).thenReturn(taskTimePriority);
        when(taskTimePriorityRepository.save(taskTimePriority)).thenReturn(taskTimePriority);
        when(taskTimePriorityMapper.toDTO(taskTimePriority)).thenReturn(taskTimePriorityDTO);
        when(userDetailsService.findUserByUsername(username)).thenReturn(user);
        when(user.getUserId()).thenReturn(1);

        TaskTimePriorityDTO createdTaskTimePriority = taskTimePriorityService
                .createTaskTimePriority(taskTimePriorityDTO, "username");

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
        taskTimePriority2.setTime(1000);
        taskTimePriority2.setPriorityId(20);

        TaskTimePriorityDTO taskTimePriorityDTO2 = new TaskTimePriorityDTO();
        taskTimePriorityDTO2.setTaskTimePriorityId(2);
        taskTimePriorityDTO2.setTime(1000L);
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

        String username = "test";

        when(taskTimePriorityRepository.findByPriorityIdAndUserId(1, 1)).thenReturn(taskTimePriority);
        when(taskTimePriorityMapper.toDTO(taskTimePriority)).thenReturn(taskTimePriorityDTO);
        when(userDetailsService.findUserByUsername(username)).thenReturn(user);
        when(user.getUserId()).thenReturn(1);

        TaskTimePriorityDTO foundTaskTimePriority = taskTimePriorityService.getTaskTimePriorityById(1, username);

        Assertions.assertNotNull(foundTaskTimePriority);
        Assertions.assertEquals(10, foundTaskTimePriority.getPriorityId());
        verify(taskTimePriorityRepository, times(1)).findByPriorityIdAndUserId(1, 1);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenGetTaskTimePriorityById() {
        String username = "test";

        when(taskTimePriorityRepository.findByPriorityIdAndUserId(1, 1)).thenReturn(null);
        when(userDetailsService.findUserByUsername(username)).thenReturn(user);
        when(user.getUserId()).thenReturn(1);

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> taskTimePriorityService.getTaskTimePriorityById(1, username));
    }

    @Test
    void shouldReturnTaskTimePriorityWhenGetTaskTimePriorityByPriorityId() {
        String username = "test";

        when(taskTimePriorityRepository.findByPriorityIdAndUserId(10,1)).thenReturn(taskTimePriority);
        when(taskTimePriorityMapper.toDTO(taskTimePriority)).thenReturn(taskTimePriorityDTO);
        when(userDetailsService.findUserByUsername(username)).thenReturn(user);
        when(user.getUserId()).thenReturn(1);

        TaskTimePriorityDTO foundTaskTimePriority = taskTimePriorityService.getTaskTimePriorityByPriorityId(10, username);

        Assertions.assertNotNull(foundTaskTimePriority);
        Assertions.assertEquals(10, foundTaskTimePriority.getPriorityId());
        verify(taskTimePriorityRepository, times(1)).findByPriorityIdAndUserId(10, 1);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenGetTaskTimePriorityByPriorityId() {

        String username = "test";

        when(taskTimePriorityRepository.findByPriorityIdAndUserId(10, 1)).thenReturn(null);
        when(userDetailsService.findUserByUsername(username)).thenReturn(user);
        when(user.getUserId()).thenReturn(1);

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> taskTimePriorityService.getTaskTimePriorityByPriorityId(10, username));
    }

    @Test
    void shouldUpdateTaskTimePriorityWhenUpdateTaskTimePriority() {
        String username = "test";
        TaskTimePriorityDTO updatedDTO = new TaskTimePriorityDTO();
        updatedDTO.setTaskTimePriorityId(1);
        updatedDTO.setTime(1000L);
        updatedDTO.setPriorityId(15);

        when(taskTimePriorityRepository.findByPriorityIdAndUserId(1, 1)).thenReturn(taskTimePriority);
        when(taskTimePriorityRepository.save(taskTimePriority)).thenReturn(taskTimePriority);
        when(taskTimePriorityMapper.toDTO(taskTimePriority)).thenReturn(updatedDTO);
        when(userDetailsService.findUserByUsername(username)).thenReturn(user);
        when(user.getUserId()).thenReturn(1);

        TaskTimePriorityDTO result = taskTimePriorityService.updateTaskTimePriority(updatedDTO, username);

        Assertions.assertNotNull(result);
        verify(taskTimePriorityMapper).updateTaskTimePriorityFromDTO(updatedDTO, taskTimePriority);
        verify(taskTimePriorityRepository).save(taskTimePriority);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdateTaskTimePriorityNotFound() {
        String username = "test";

        when(taskTimePriorityRepository.findByPriorityIdAndUserId(1, 1)).thenReturn(null);
        when(userDetailsService.findUserByUsername(username)).thenReturn(user);
        when(user.getUserId()).thenReturn(1);

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> taskTimePriorityService.updateTaskTimePriority(taskTimePriorityDTO, username));
    }

    @Test
    void verifyDeleteTaskTimePriority() {
        String username = "test";

        when(taskTimePriorityRepository.findByPriorityIdAndUserId(1, 1)).thenReturn(taskTimePriority);
        when(taskTimePriorityMapper.toDTO(taskTimePriority)).thenReturn(taskTimePriorityDTO);
        when(userDetailsService.findUserByUsername(username)).thenReturn(user);
        when(user.getUserId()).thenReturn(1);

        taskTimePriorityService.deleteTaskTimePriorityById(1, username);

        verify(taskTimePriorityRepository, times(1)).deleteById(1);
        verify(taskTimePriorityRepository, times(1)).findByPriorityIdAndUserId(1, 1);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenDeleteTaskTimePriorityNotFound() {
        String username = "test";

        when(taskTimePriorityRepository.findByPriorityIdAndUserId(1, 1)).thenReturn(null);
        when(userDetailsService.findUserByUsername(username)).thenReturn(user);
        when(user.getUserId()).thenReturn(1);

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> taskTimePriorityService.deleteTaskTimePriorityById(1, username));
    }

    @Test
    void shouldReturnTaskTimePriorityListWhenGetTaskTimePrioritiesByUserId() {
        when(taskTimePriorityRepository.findByUserId(100)).thenReturn(List.of(taskTimePriority));
        when(taskTimePriorityMapper.toDTO(taskTimePriority)).thenReturn(taskTimePriorityDTO);
        when(userDetailsService.findUserByUsername("usertest")).thenReturn(user);
        when(user.getUserId()).thenReturn(100);
        List<TaskTimePriorityDTO> result = taskTimePriorityService.getTaskTimePrioritiesByUser("usertest");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        verify(taskTimePriorityRepository, times(1)).findByUserId(100);
    }
}
