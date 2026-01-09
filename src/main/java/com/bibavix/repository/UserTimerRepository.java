package com.bibavix.repository;

import com.bibavix.model.User;
import com.bibavix.model.UserTimer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTimerRepository extends JpaRepository<UserTimer, Integer> {
    Optional<UserTimer> findByUserId(int userId);
}
