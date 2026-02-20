package com.bibavix.repository;

import com.bibavix.model.TaskStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StatusRepository extends CrudRepository<TaskStatus, UUID> {
}
