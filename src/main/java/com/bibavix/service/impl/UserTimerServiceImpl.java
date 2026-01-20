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

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
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
    public UserTimerDTO startTimer(Integer userId) {
        UserTimer userTimer = userTimerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserTimer not found for userId " + userId));

        if (Boolean.TRUE.equals(userTimer.getIsRunning())) {
            throw new IllegalStateException("Timer is already running.");
        }
        if (userTimer.getTotalSecondsAccumulated() <= 0) {
            throw new IllegalStateException("No accumulated time to start.");
        }

        syncTimerState(userTimer);

        userTimer.setIsRunning(true);
        userTimer.setStartedAt(Timestamp.from(Instant.now()));
        userTimer.setUpdatedAt(Timestamp.from(Instant.now()));

        UserTimer savedTimer = userTimerRepository.save(userTimer);
        UserTimerDTO dto = userTimerMapper.toDTO(savedTimer);
        dto.setRemainingSeconds(Long.valueOf(savedTimer.getTotalSecondsAccumulated()));

        return dto;
    }


    @Override
    @Transactional
    public UserTimerDTO pauseTimer(Integer userId) {

        UserTimer userTimer = userTimerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserTimer not found"));

        if (!Boolean.TRUE.equals(userTimer.getIsRunning())) {
            throw new IllegalStateException("Timer is not running.");
        }

        long remainingSeconds = syncTimerState(userTimer);

        userTimer.setTotalSecondsAccumulated((int) remainingSeconds);
        userTimer.setIsRunning(false);
        userTimer.setStartedAt(null);
        userTimer.setUpdatedAt(Timestamp.from(Instant.now()));

        userTimerRepository.save(userTimer);

        UserTimerDTO dto = userTimerMapper.toDTO(userTimer);
        dto.setRemainingSeconds(remainingSeconds);

        return dto;
    }



    @Override
    @Transactional
    public UserTimerDTO getTimerStatus(Integer userId) {

        UserTimer userTimer = userTimerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "UserTimer not found for userId " + userId
                ));

        long remainingSeconds = syncTimerState(userTimer);

        UserTimerDTO dto = userTimerMapper.toDTO(userTimer);
        dto.setRemainingSeconds(remainingSeconds);

        return dto;
    }

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

        int secondsToAdd = taskTimePriorityRepository.findByPriorityIdAndUserId(priority, user.getUserId()).getTime();
        if (secondsToAdd <= 0) {
            return; // nada que sumar
        }
        int updatedTotalSeconds = userTimer.getTotalSecondsAccumulated() + secondsToAdd;

        userTimer.setTotalSecondsAccumulated(updatedTotalSeconds);

        // 5. Persistir
        userTimerRepository.save(userTimer);
    }

    private long syncTimerState(UserTimer userTimer) {

        if (!Boolean.TRUE.equals(userTimer.getIsRunning())
                || userTimer.getStartedAt() == null) {
            return userTimer.getTotalSecondsAccumulated();
        }

        long elapsedSeconds = Duration.between(
                userTimer.getStartedAt().toInstant(),
                Instant.now()
        ).getSeconds();

        long remainingSeconds =
                userTimer.getTotalSecondsAccumulated() - elapsedSeconds;

        if (remainingSeconds <= 0) {
            // El timer expiró (posiblemente durante una caída)
            userTimer.setTotalSecondsAccumulated(0);
            userTimer.setIsRunning(false);
            userTimer.setStartedAt(null);
            userTimer.setUpdatedAt(Timestamp.from(Instant.now()));

            userTimerRepository.save(userTimer);

            // Aquí luego dispararemos el evento
            return 0;
        }

        return remainingSeconds;
    }

}
