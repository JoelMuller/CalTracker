package com.jamgm.CalTracker.repository;

import com.jamgm.CalTracker.config.TestApplicationContext;
import com.jamgm.CalTracker.model.User;
import com.jamgm.CalTracker.service.OpenFoodFactsApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = TestApplicationContext.class)
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private OpenFoodFactsApiService openFoodFactsApiService;

    //Note: Make sure database you're using is empty otherwise tests fail
    @BeforeEach
    public void beforeEach() {
        User user1 = User.builder()
                .name("testuser1")
                .email("test1@test.com")
                .password("test")
                .weightLossPerWeek(0.50)
                .build();
        userRepository.save(user1);
        User user2 = User.builder()
                .name("testuser2")
                .email("test2@test.com")
                .password("test")
                .weightLossPerWeek(1.0)
                .build();
        userRepository.save(user2);
    }

    @Test
    public void testCreateUserAndGetById(){
        User user = User.builder()
                .name("testuser3")
                .email("test3@test.com")
                .password("test")
                .weightLossPerWeek(1.50)
                .build();
        int oldSize = userRepository.findAll().size();
        User savedUser = userRepository.save(user);
        assertNotEquals(userRepository.findAll().size(), oldSize);
        assertEquals(userRepository.findById(savedUser.getId()).get().getName(), "testuser3");
    }

    @Test
    public void testGetUserByName(){
        User user = userRepository.findByName("testuser1");
        assertEquals(user.getName(), "testuser1");
    }

    @Test
    public void testGetUserByNameFail(){
        User user = userRepository.findByName("doesNotExist");
        assertThrows(NullPointerException.class, () -> user.getName());
    }

    @Test
    public void testUpdateUser(){
        User user = userRepository.findByName("testuser1");
        user.setEmail("changed@test.com");
        userRepository.save(user); //happens implicitly by entitymanager but still added to make test fucntion clear

        assertEquals(userRepository.findByName("testuser1").getEmail(), "changed@test.com");
    }

    @Test
    public void testDeleteUser(){
        User user = User.builder()
                .name("testUserToBeDeleted")
                .build();

        int totalUsers = userRepository.findAll().size();
        userRepository.save(user);
        assertNotEquals(userRepository.findAll().size(), totalUsers);

        userRepository.delete(user);
        assertEquals(userRepository.findAll().size(), totalUsers);
    }
}
