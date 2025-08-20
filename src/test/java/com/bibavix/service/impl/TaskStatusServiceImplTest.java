package com.bibavix.service.impl;

import com.bibavix.dto.TaskStatusDTO;
import com.bibavix.exception.TaskStatusNotFoundException;
import com.bibavix.model.TaskStatus;
import com.bibavix.repository.TaskStatusRepository;
import com.bibavix.util.mapper.TaskStatusMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskStatusServiceImplTest {

    @InjectMocks
    TaskStatusServiceImpl taskStatusService;

    @Mock
    TaskStatusRepository taskStatusRepository;

    @Mock
    TaskStatusMapper taskStatusMapper;

    @Test
    void shouldReturnTaskStatusDTOWhenCreateStatusTask(){
        TaskStatus taskStatus = new TaskStatus(1, "Done");
        TaskStatusDTO taskStatusDTO = new TaskStatusDTO(1, "Done");
        when(taskStatusMapper.toEntity(taskStatusDTO)).thenReturn(taskStatus);
        when(taskStatusRepository.save(taskStatus)).thenReturn(taskStatus);
        when(taskStatusMapper.toDTO(taskStatus)).thenReturn(taskStatusDTO);
        TaskStatusDTO taskStatusCreated = taskStatusService.createTaskStatus(taskStatusDTO);
        Assertions.assertNotNull(taskStatusCreated);
        verify(taskStatusMapper, times(1)).toEntity(any());
        verify(taskStatusRepository, times(1)).save(any());
        verify(taskStatusMapper, times(1)).toDTO(any());
    }

    @Test
    void shouldReturnTaskStatusListWhenGetAllTaskStatus() {
        List<TaskStatus> taskStatusList = List
                .of(new TaskStatus(1, "Done"),
                    new TaskStatus(2, "In progress"));
        when(taskStatusRepository.findAll()).thenReturn(taskStatusList);
        List<TaskStatusDTO> taskStatusDTOS = taskStatusService.getAllTaskStatus();
        Assertions.assertNotNull(taskStatusDTOS);
        Assertions.assertFalse(taskStatusDTOS.isEmpty());
        Assertions.assertEquals(2, taskStatusDTOS.size());
        verify(taskStatusRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnTaskStatusWhenGetTaskStatusById() {
        TaskStatus taskStatus = new TaskStatus(1, "Done");
        TaskStatusDTO taskStatusDTO = new TaskStatusDTO(1, "Done");
        when(taskStatusRepository.findById(1)).thenReturn(Optional.of(taskStatus));
        when(taskStatusMapper.toDTO(taskStatus)).thenReturn(taskStatusDTO);
        TaskStatusDTO taskStatusDTOFound = taskStatusService.getTaskStatusById(1);
        Assertions.assertNotNull(taskStatusDTOFound);
        Assertions.assertEquals("Done", taskStatusDTOFound.getName());
        verify(taskStatusRepository, times(1)).findById(1);
    }

    @Test
    void shouldThrowTaskStatusNotFoundExceptionWhenGetTaskStatusById() {
        when(taskStatusRepository.findById(1)).thenThrow(new TaskStatusNotFoundException(1));
        Assertions.assertThrows(TaskStatusNotFoundException.class,() ->
                taskStatusService.getTaskStatusById(1));
    }

    @Test
    void verifyDeleteTaskStatus(){
        TaskStatus taskStatus = new TaskStatus(1, "Done");
        TaskStatusDTO taskStatusDTO = new TaskStatusDTO(1, "Done");
        when(taskStatusRepository.findById(1)).thenReturn(Optional.of(taskStatus));
        when(taskStatusMapper.toDTO(taskStatus)).thenReturn(taskStatusDTO);

        taskStatusService.deleteTaskStatus(1);
        verify(taskStatusRepository, times(1)).deleteById(1);
        verify(taskStatusRepository, times(1)).findById(1);
    }

}
