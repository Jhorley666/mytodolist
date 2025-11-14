package com.bibavix.repository;

import com.bibavix.model.TaskTimePriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskTimePriorityRepository extends JpaRepository<TaskTimePriority, Integer> {
    TaskTimePriority findByPriorityId(Integer priorityId);
}
