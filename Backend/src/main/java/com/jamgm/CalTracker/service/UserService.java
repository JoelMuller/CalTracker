package com.jamgm.CalTracker.service;

import com.jamgm.CalTracker.model.Role;
import com.jamgm.CalTracker.model.User;
import com.jamgm.CalTracker.repository.UserRepository;
import com.jamgm.CalTracker.web.rest.DTO.UserDTO;
import com.jamgm.CalTracker.web.rest.transformer.UserTransformer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public UserDTO createUser(UserDTO user){
        if(userRepository.existsByEmail(user.getEmail())){
            throw new IllegalArgumentException("Email already in use");
        }
        user.setRole(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return UserTransformer.toDto(userRepository.save(UserTransformer.fromDto(user)));
    }

    public Boolean checkEmailExists(String email){
        return userRepository.existsByEmail(email);
    }

    public UserDTO getUserById(long userId){
        if(userRepository.existsById(userId)){
            return UserTransformer.toDto(userRepository.findById(userId).get());
        }else{
            throw new RuntimeException("User with id: " + userId + " does not exist");
        }
    }

    public UserDTO updateUser(UserDTO user){
        if(userRepository.existsById(user.getId())){
            if(!user.getPassword().isEmpty()) user.setPassword(passwordEncoder.encode(user.getPassword()));
            return UserTransformer.toDto(userRepository.save(UserTransformer.fromDto(user)));
        }else{
            throw new RuntimeException("User with id: " + user.getId() + " does not exist");
        }
    }

    public void deleteUser(long userId){
        if(userRepository.existsById(userId)){
            userRepository.deleteById(userId);
        }else{
            throw new RuntimeException("User with id: " + userId + " does not exist");
        }
    }
}
