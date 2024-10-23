package com.jamgm.CalTracker.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamgm.CalTracker.Interceptors.RateLimitFilter;
import com.jamgm.CalTracker.authentication.JwtRequestFilter;
import com.jamgm.CalTracker.authentication.JwtUtil;
import com.jamgm.CalTracker.model.LoginRequest;
import com.jamgm.CalTracker.model.User;
import com.jamgm.CalTracker.service.CustomUserDetailsService;
import com.jamgm.CalTracker.service.UserService;
import com.jamgm.CalTracker.web.rest.DTO.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private RateLimitFilter rateLimitFilter;
    @MockBean
    private JwtRequestFilter jwtRequestFilter;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private CustomUserDetailsService userDetailsService;
    @MockBean
    private UserDetails userDetails;
    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private User user;
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
                .weight(80.0)
                .weightLossPerWeek(0.5)
                .build();
        this.userDTOWithoutid = UserDTO.builder()
                .name("testuser")
                .email("test@test.com")
                .password("testing")
                .weight(80.0)
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
        this.mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTOWithoutid)))
                .andExpect(status().isOk())
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

    @Test
    public void testLoginSuccess() throws Exception {
        // Given
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        // Simulate successful authentication
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(Mockito.mock(UsernamePasswordAuthenticationToken.class));

        // Simulate loading user details
        when(userDetailsService.loadUserByUsername(loginRequest.getEmail())).thenReturn(userDetails);
        doReturn(List.of((GrantedAuthority) () -> "ROLE_USER")).when(userDetails).getAuthorities();
        when(jwtUtil.generateToken(userDetails)).thenReturn("mockJwtToken");
        when(userDetailsService.getUserByEmail(loginRequest.getEmail())).thenReturn(user);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(user.getId()).thenReturn(1L);

        // When & Then
        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json("{'token': 'mockJwtToken', 'username': 'test@example.com', 'userId': 1, 'roles': ['ROLE_USER']}"));
    }

    @Test
    public void testLoginInvalidCredentials() throws Exception {
        // Given
        LoginRequest loginRequest = LoginRequest.builder()
                .email("wrong@example.com")
                .password("wrongpassword")
                .build();

        // Simulate authentication failure
        doThrow(new BadCredentialsException("Invalid credentials"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        // When & Then
        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    public void testLoginInternalServerError() throws Exception {
        // Given
        LoginRequest loginRequest = LoginRequest.builder()
                .email("error@example.com")
                .password("password")
                .build();

        // Simulate unexpected exception
        doThrow(new RuntimeException("Something went wrong"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        // When & Then
        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isInternalServerError());
    }
}
