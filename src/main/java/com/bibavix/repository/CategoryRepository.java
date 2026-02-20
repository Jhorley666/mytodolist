package com.bibavix.repository;

import com.bibavix.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category, UUID> {
    List<Category> findAllByUserId(UUID userId);
}
