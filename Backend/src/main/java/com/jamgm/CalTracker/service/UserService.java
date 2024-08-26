package com.jamgm.CalTracker.service;

import com.jamgm.CalTracker.model.User;
import com.jamgm.CalTracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(User user){
        return userRepository.save(user);
    }

    public Optional<User> getUserById(long userId){
        if(userRepository.existsById(userId)){
            return userRepository.findById(userId);
        }else{
            throw new RuntimeException("User with id: " + userId + " does not exist");
        }
    }

    public User updateUser(User user){
        if(userRepository.existsById(user.getId())){
            if(!user.getPassword().isEmpty()) user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }else{
            throw new RuntimeException("User with id: " + user.getId() + " does not exist");
        }
    }

    public void deleteUser(long userId){
        userRepository.deleteById(userId);
    }
}
