package com.bibavix.repository;

import com.bibavix.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus, UUID> {
}
