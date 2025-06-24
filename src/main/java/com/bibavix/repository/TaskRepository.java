package com.bibavix.repository;

import com.bibavix.model.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {
    List<Task> findAllByUserId(Integer userId);
}