package com.bibavix.repository;

import com.bibavix.model.TaskStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends CrudRepository <TaskStatus, Integer> {
}
