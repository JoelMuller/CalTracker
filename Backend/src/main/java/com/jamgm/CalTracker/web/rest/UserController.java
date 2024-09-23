package com.jamgm.CalTracker.web.rest;

import com.jamgm.CalTracker.authentication.JwtResponse;
import com.jamgm.CalTracker.authentication.JwtUtil;
import com.jamgm.CalTracker.model.LoginRequest;
import com.jamgm.CalTracker.model.User;
import com.jamgm.CalTracker.service.CustomUserDetailsService;
import com.jamgm.CalTracker.service.UserService;
import com.jamgm.CalTracker.web.rest.DTO.UserDTO;
import com.jamgm.CalTracker.web.rest.transformer.UserTransformer;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
            String jwt = jwtUtil.generateToken(userDetails);
            User user = userDetailsService.getUserByEmail(loginRequest.getEmail());

            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), user.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping(value = "/check-email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable("email") final String email) {
        return ResponseEntity.ok(this.userService.checkEmailExists(email));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") final long id) {
        return ResponseEntity.ok(this.userService.getUserById(id));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            this.userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("User " + userDTO.getName() + " created");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody @Valid UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.updateUser(userDTO));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") final long id) {
        this.userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
