package com.jamgm.CalTracker.repository;

import com.jamgm.CalTracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
    Optional<User> findByEmail(String email);
}
