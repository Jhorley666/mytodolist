package com.bibavix.service.impl;

import com.bibavix.dto.UserTimerDTO;
import com.bibavix.exception.ResourceNotFoundException;
import com.bibavix.model.Task;
import com.bibavix.model.User;
import com.bibavix.model.UserTimer;
import com.bibavix.repository.TaskTimePriorityRepository;
import com.bibavix.repository.UserTimerRepository;
import com.bibavix.service.PriorityService;
import com.bibavix.service.UserTimerService;
import com.bibavix.util.mapper.UserTimerMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserTimerServiceImpl implements UserTimerService {

    private final UserTimerRepository userTimerRepository;
    private final UserTimerMapper userTimerMapper;
    private final PriorityService priorityService;
    private final TaskTimePriorityRepository taskTimePriorityRepository;

    @Override
    public UserTimerDTO createUserTimer(UserTimerDTO userTimerDTO) {
        UserTimer userTimerToSave = userTimerMapper.toEntity(userTimerDTO);
        UserTimer savedUserTimer = userTimerRepository.save(userTimerToSave);
        return userTimerMapper.toDTO(savedUserTimer);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserTimerDTO> getAllUserTimers() {
        List<UserTimer> userTimerList = userTimerRepository.findAll();
        return userTimerList.stream()
                .map(userTimerMapper::toDTO)
                .toList();
    }

    @Override
    public UserTimerDTO getUserTimerById(Integer userTimerId) {
        UserTimer userTimer = userTimerRepository.findById(userTimerId)
                .orElseThrow(() -> new ResourceNotFoundException("UserTimer with id " + userTimerId + " not found."));
        return userTimerMapper.toDTO(userTimer);
    }

    @Override
    public UserTimerDTO updateUserTimer(UserTimerDTO userTimerDTO) {
        Integer userTimerId = userTimerDTO.getIdUserTimer();
        UserTimer userTimer = userTimerRepository.findById(userTimerId)
                .orElseThrow(() -> new ResourceNotFoundException("UserTimer with id " + userTimerId + " not found."));

        userTimerMapper.updateUserTimerFromDTO(userTimerDTO, userTimer);
        UserTimer updatedUserTimer = userTimerRepository.save(userTimer);
        return userTimerMapper.toDTO(updatedUserTimer);
    }

    @Override
    public void deleteUserTimerById(Integer userTimerId) {
        if (!userTimerRepository.existsById(userTimerId)) {
            throw new ResourceNotFoundException("UserTimer with id " + userTimerId + " not found.");
        }
        userTimerRepository.deleteById(userTimerId);
    }

    @Override
    public void onTaskCompleted(Task updatedTask, User user) {
        UserTimer userTimer = userTimerRepository.findByUserId(user.getUserId())
                .orElseGet(() -> {
                    UserTimer newTimer = new UserTimer();
                    newTimer.setUserId(user.getUserId());
                    newTimer.setTotalSecondsAccumulated(0);
                    newTimer.setIsRunning(false);
                    newTimer.setStartedAt(null);
                    return userTimerRepository.save(newTimer);
                });

        var priority = updatedTask.getPriorityId();
        if (Objects.isNull(priority)) {
            return;
        }

        int secondsToAdd = taskTimePriorityRepository.findByPriorityId(priority.intValue()).getTime();
        if (secondsToAdd <= 0) {
            return; // nada que sumar
        }
        int updatedTotalSeconds =
                userTimer.getTotalSecondsAccumulated() + secondsToAdd;

        userTimer.setTotalSecondsAccumulated(updatedTotalSeconds);

        // 5. Persistir
        userTimerRepository.save(userTimer);
    }
}
