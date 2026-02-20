package com.bibavix.repository;

import com.bibavix.model.TaskTimePriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface TaskTimePriorityRepository extends JpaRepository<TaskTimePriority, UUID> {
    TaskTimePriority findByPriorityId(UUID priorityId);

    TaskTimePriority findByPriorityIdAndUserId(UUID priorityId, UUID userId);

    List<TaskTimePriority> findByUserId(UUID userId);
}
