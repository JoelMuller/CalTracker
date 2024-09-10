package com.jamgm.CalTracker.web.rest;

import com.jamgm.CalTracker.model.LoginRequest;
import com.jamgm.CalTracker.model.User;
import com.jamgm.CalTracker.service.UserService;
import com.jamgm.CalTracker.web.rest.DTO.UserDTO;
import com.jamgm.CalTracker.web.rest.transformer.UserTransformer;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, AuthenticationManager authenticationManager){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword())
            );

            return ResponseEntity.ok("login successful");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid credentials");
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") final long id){
        return ResponseEntity.ok(this.userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody @Valid UserDTO userDTO){
        this.userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User " + userDTO.getName() + " created");
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody @Valid UserDTO userDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.updateUser(userDTO));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") final long id){
        this.userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
