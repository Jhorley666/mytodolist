package com.bibavix.repository;

import com.bibavix.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository <Category, Integer>{
    List<Category> findAllByUserId(Integer userId);
}
