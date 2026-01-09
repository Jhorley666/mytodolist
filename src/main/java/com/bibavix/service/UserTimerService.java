package com.bibavix.service;

import com.bibavix.dto.UserTimerDTO;
import com.bibavix.model.Task;
import com.bibavix.model.User;

import java.util.List;

public interface UserTimerService {
    UserTimerDTO createUserTimer(UserTimerDTO userTimerDTO);

    List<UserTimerDTO> getAllUserTimers();

    UserTimerDTO getUserTimerById(Integer userTimerId);

    UserTimerDTO updateUserTimer(UserTimerDTO userTimerDTO);

    void deleteUserTimerById(Integer userTimerId);

    void onTaskCompleted(Task updatedTask, User user);
}
