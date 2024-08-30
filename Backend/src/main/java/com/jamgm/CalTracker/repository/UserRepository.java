package com.jamgm.CalTracker.repository;

import com.jamgm.CalTracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
}
