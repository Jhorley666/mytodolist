package com.bibavix.service;

import java.util.UUID;

import com.bibavix.dto.UserTimerDTO;
import com.bibavix.model.Task;
import com.bibavix.model.User;

import java.util.List;

public interface UserTimerService {
    UserTimerDTO createUserTimer(UserTimerDTO userTimerDTO);

    List<UserTimerDTO> getAllUserTimers();

    UserTimerDTO getUserTimerById(UUID userTimerId);

    UserTimerDTO updateUserTimer(UserTimerDTO userTimerDTO);

    void deleteUserTimerById(UUID userTimerId);

    void onTaskCompleted(Task updatedTask, User user);

    UserTimerDTO startTimer(UUID userId);

    UserTimerDTO getTimerStatus(UUID userId);

    UserTimerDTO pauseTimer(UUID userId);
}
