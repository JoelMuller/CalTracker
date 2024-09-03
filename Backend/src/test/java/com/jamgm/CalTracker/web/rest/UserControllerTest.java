package com.jamgm.CalTracker.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamgm.CalTracker.service.UserService;
import com.jamgm.CalTracker.web.rest.DTO.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;

    private UserDTO userDTO;
    private UserDTO userDTOWithoutid;
    @BeforeEach
    public void setup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.userDTO = UserDTO.builder()
                .id(1L)
                .name("testuser")
                .email("test@test.com")
                .password("testing")
                .weightLossPerWeek(0.5)
                .build();
        this.userDTOWithoutid = UserDTO.builder()
                .name("testuser")
                .email("test@test.com")
                .password("testing")
                .weightLossPerWeek(0.5)
                .build();
    }
    @Test
    public void testGetUserById() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userDTO);
        this.mockMvc.perform(get("/user/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testuser"));
    }

    @Test
    public void testCreateUser() throws Exception {
        when(userService.createUser(userDTOWithoutid)).thenReturn(userDTO);
        this.mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTOWithoutid)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("User testuser created")));
    }

    @Test
    public void testUpdateUser() throws Exception {
        when(userService.updateUser(any(UserDTO.class))).thenReturn(userDTO);
        this.mockMvc.perform(put("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testDeleteUserById() throws Exception {
        doNothing().when(userService).deleteUser(isA(Long.class));
        this.mockMvc.perform(delete("/user/{id}", 1))
                .andExpect(status().isNoContent());
    }
}
