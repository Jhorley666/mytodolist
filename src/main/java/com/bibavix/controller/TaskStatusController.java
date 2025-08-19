package com.bibavix.controller;

import com.bibavix.dto.TaskStatusDTO;
import com.bibavix.service.TaskStatusService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/status")
public class TaskStatusController {

    private final TaskStatusService taskStatusService;

    public TaskStatusController(TaskStatusService taskStatusService) {
        this.taskStatusService = taskStatusService;
    }

    @GetMapping
    public List<TaskStatusDTO> getAllTaskStatus() {
        return taskStatusService.getAllTaskStatus();
    }
}
