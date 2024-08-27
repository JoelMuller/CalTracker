package com.jamgm.CalTracker.web.rest;

import com.jamgm.CalTracker.model.User;
import com.jamgm.CalTracker.service.UserService;
import com.jamgm.CalTracker.web.rest.DTO.UserDTO;
import com.jamgm.CalTracker.web.rest.transformer.UserTransformer;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping(value = "/{id}")
    public UserDTO getUserById(@PathVariable("id") final long id){
        return UserTransformer.toDto(this.userService.getUserById(id).get());
    }

    @PostMapping
    public void createUser(@RequestBody @Valid UserDTO userDTO){
        this.userService.createUser(UserTransformer.fromDto(userDTO));
    }

    @PutMapping
    public UserDTO updateUser(@RequestBody @Valid UserDTO userDTO){
        return UserTransformer.toDto(
                this.userService.updateUser(
                        UserTransformer.fromDto(userDTO)));
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUserById(@PathVariable("id") final long id){
        this.userService.deleteUser(id);
    }
}
