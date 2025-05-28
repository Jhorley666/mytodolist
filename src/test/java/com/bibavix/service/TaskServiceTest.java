package com.bibavix.service;

import com.bibavix.dto.TaskDTO;
import com.bibavix.model.Task;
import com.bibavix.repository.TaskRepository;
import com.bibavix.util.mapper.TaskMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @InjectMocks
    TaskService taskService;
    @Mock
    TaskRepository taskRepository;
    @Mock
    TaskMapper taskMapper;
    @Mock
    TaskDTO taskDTO;
    @Mock
    Task task;

    @Test
    void shouldReturnLongWhenTaskIsCreated() {
        when(taskMapper.toEntity(taskDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        Integer taskId = taskService.createTask(taskDTO, 1);
        assertNotNull(taskId);
    }

}