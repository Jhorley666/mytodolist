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
import java.util.UUID;

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

    private final UUID taskTimePriorityId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final UUID priorityId = UUID.fromString("00000000-0000-0000-0000-000000000010");
    private final UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000100");
    private final UUID diffPriorityId = UUID.fromString("00000000-0000-0000-0000-000000000015");
    private final UUID taskTimePriorityId2 = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private final UUID priorityId2 = UUID.fromString("00000000-0000-0000-0000-000000000020");

    @BeforeEach
    void setUp() {
        Integer testDate = 1000;

        taskTimePriority = new TaskTimePriority();
        taskTimePriority.setTaskTimePriorityId(taskTimePriorityId);
        taskTimePriority.setTime(testDate);
        taskTimePriority.setPriorityId(priorityId);
        taskTimePriority.setUserId(userId);

        taskTimePriorityDTO = new TaskTimePriorityDTO();
        taskTimePriorityDTO.setTaskTimePriorityId(taskTimePriorityId);
        taskTimePriorityDTO.setTime(1000L);
        taskTimePriorityDTO.setPriorityId(priorityId);
        taskTimePriorityDTO.setUserId(userId);
    }

    @Test
    void shouldReturnTaskTimePriorityDTOWhenCreateTaskTimePriority() {
        String username = "username";

        when(taskTimePriorityMapper.toEntity(taskTimePriorityDTO)).thenReturn(taskTimePriority);
        when(taskTimePriorityRepository.save(taskTimePriority)).thenReturn(taskTimePriority);
        when(taskTimePriorityMapper.toDTO(taskTimePriority)).thenReturn(taskTimePriorityDTO);
        when(userDetailsService.findUserByUsername(username)).thenReturn(user);
        when(user.getUserId()).thenReturn(userId);

        TaskTimePriorityDTO createdTaskTimePriority = taskTimePriorityService
                .createTaskTimePriority(taskTimePriorityDTO, "username");

        Assertions.assertNotNull(createdTaskTimePriority);
        Assertions.assertEquals(priorityId, createdTaskTimePriority.getPriorityId());
        verify(taskTimePriorityMapper, times(1)).toEntity(any());
        verify(taskTimePriorityRepository, times(1)).save(any());
        verify(taskTimePriorityMapper, times(1)).toDTO(any());
    }

    @Test
    void shouldReturnTaskTimePriorityListWhenGetAllTaskTimePriorities() {
        TaskTimePriority taskTimePriority2 = new TaskTimePriority();
        taskTimePriority2.setTaskTimePriorityId(taskTimePriorityId2);
        taskTimePriority2.setTime(1000);
        taskTimePriority2.setPriorityId(priorityId2);

        TaskTimePriorityDTO taskTimePriorityDTO2 = new TaskTimePriorityDTO();
        taskTimePriorityDTO2.setTaskTimePriorityId(taskTimePriorityId2);
        taskTimePriorityDTO2.setTime(1000L);
        taskTimePriorityDTO2.setPriorityId(priorityId2);

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

        when(taskTimePriorityRepository.findByPriorityIdAndUserId(taskTimePriorityId, userId))
                .thenReturn(taskTimePriority);
        when(taskTimePriorityMapper.toDTO(taskTimePriority)).thenReturn(taskTimePriorityDTO);
        when(userDetailsService.findUserByUsername(username)).thenReturn(user);
        when(user.getUserId()).thenReturn(userId);

        TaskTimePriorityDTO foundTaskTimePriority = taskTimePriorityService.getTaskTimePriorityById(taskTimePriorityId,
                username);

        Assertions.assertNotNull(foundTaskTimePriority);
        Assertions.assertEquals(priorityId, foundTaskTimePriority.getPriorityId());
        verify(taskTimePriorityRepository, times(1)).findByPriorityIdAndUserId(taskTimePriorityId, userId);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenGetTaskTimePriorityById() {
        String username = "test";

        when(taskTimePriorityRepository.findByPriorityIdAndUserId(taskTimePriorityId, userId)).thenReturn(null);
        when(userDetailsService.findUserByUsername(username)).thenReturn(user);
        when(user.getUserId()).thenReturn(userId);

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> taskTimePriorityService.getTaskTimePriorityById(taskTimePriorityId, username));
    }

    @Test
    void shouldReturnTaskTimePriorityWhenGetTaskTimePriorityByPriorityId() {
        String username = "test";

        when(taskTimePriorityRepository.findByPriorityIdAndUserId(priorityId, userId)).thenReturn(taskTimePriority);
        when(taskTimePriorityMapper.toDTO(taskTimePriority)).thenReturn(taskTimePriorityDTO);
        when(userDetailsService.findUserByUsername(username)).thenReturn(user);
        when(user.getUserId()).thenReturn(userId);

        TaskTimePriorityDTO foundTaskTimePriority = taskTimePriorityService.getTaskTimePriorityByPriorityId(priorityId,
                username);

        Assertions.assertNotNull(foundTaskTimePriority);
        Assertions.assertEquals(priorityId, foundTaskTimePriority.getPriorityId());
        verify(taskTimePriorityRepository, times(1)).findByPriorityIdAndUserId(priorityId, userId);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenGetTaskTimePriorityByPriorityId() {

        String username = "test";

        when(taskTimePriorityRepository.findByPriorityIdAndUserId(priorityId, userId)).thenReturn(null);
        when(userDetailsService.findUserByUsername(username)).thenReturn(user);
        when(user.getUserId()).thenReturn(userId);

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> taskTimePriorityService.getTaskTimePriorityByPriorityId(priorityId, username));
    }

    @Test
    void shouldUpdateTaskTimePriorityWhenUpdateTaskTimePriority() {
        String username = "test";
        TaskTimePriorityDTO updatedDTO = new TaskTimePriorityDTO();
        updatedDTO.setTaskTimePriorityId(taskTimePriorityId);
        updatedDTO.setTime(1000L);
        updatedDTO.setPriorityId(diffPriorityId);

        when(taskTimePriorityRepository.findByPriorityIdAndUserId(taskTimePriorityId, userId))
                .thenReturn(taskTimePriority);
        when(taskTimePriorityRepository.save(taskTimePriority)).thenReturn(taskTimePriority);
        when(taskTimePriorityMapper.toDTO(taskTimePriority)).thenReturn(updatedDTO);
        when(userDetailsService.findUserByUsername(username)).thenReturn(user);
        when(user.getUserId()).thenReturn(userId);

        TaskTimePriorityDTO result = taskTimePriorityService.updateTaskTimePriority(updatedDTO, username);

        Assertions.assertNotNull(result);
        verify(taskTimePriorityMapper).updateTaskTimePriorityFromDTO(updatedDTO, taskTimePriority);
        verify(taskTimePriorityRepository).save(taskTimePriority);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdateTaskTimePriorityNotFound() {
        String username = "test";

        when(taskTimePriorityRepository.findByPriorityIdAndUserId(taskTimePriorityId, userId)).thenReturn(null);
        when(userDetailsService.findUserByUsername(username)).thenReturn(user);
        when(user.getUserId()).thenReturn(userId);

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> taskTimePriorityService.updateTaskTimePriority(taskTimePriorityDTO, username));
    }

    @Test
    void verifyDeleteTaskTimePriority() {
        String username = "test";

        when(taskTimePriorityRepository.findByPriorityIdAndUserId(taskTimePriorityId, userId))
                .thenReturn(taskTimePriority);
        when(taskTimePriorityMapper.toDTO(taskTimePriority)).thenReturn(taskTimePriorityDTO);
        when(userDetailsService.findUserByUsername(username)).thenReturn(user);
        when(user.getUserId()).thenReturn(userId);

        taskTimePriorityService.deleteTaskTimePriorityById(taskTimePriorityId, username);

        verify(taskTimePriorityRepository, times(1)).deleteById(taskTimePriorityId);
        verify(taskTimePriorityRepository, times(1)).findByPriorityIdAndUserId(taskTimePriorityId, userId);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenDeleteTaskTimePriorityNotFound() {
        String username = "test";

        when(taskTimePriorityRepository.findByPriorityIdAndUserId(taskTimePriorityId, userId)).thenReturn(null);
        when(userDetailsService.findUserByUsername(username)).thenReturn(user);
        when(user.getUserId()).thenReturn(userId);

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> taskTimePriorityService.deleteTaskTimePriorityById(taskTimePriorityId, username));
    }

    @Test
    void shouldReturnTaskTimePriorityListWhenGetTaskTimePrioritiesByUserId() {
        when(taskTimePriorityRepository.findByUserId(userId)).thenReturn(List.of(taskTimePriority));
        when(taskTimePriorityMapper.toDTO(taskTimePriority)).thenReturn(taskTimePriorityDTO);
        when(userDetailsService.findUserByUsername("usertest")).thenReturn(user);
        when(user.getUserId()).thenReturn(userId);
        List<TaskTimePriorityDTO> result = taskTimePriorityService.getTaskTimePrioritiesByUser("usertest");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        verify(taskTimePriorityRepository, times(1)).findByUserId(userId);
    }
}
