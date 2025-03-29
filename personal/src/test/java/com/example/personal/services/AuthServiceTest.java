package com.example.personal.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Map;
import java.util.Optional;

import com.example.personal.models.User;
import com.example.personal.repositories.UserRepository;
import com.example.personal.security.JwtUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private BCryptPasswordEncoder passwordEncoder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    public void testUserExists_UserExists_ReturnsTrue() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        assertTrue(authService.userExists(email));
    }

    @Test
    public void testUserExists_UserDoesNotExist_ReturnsFalse() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertFalse(authService.userExists(email));
    }

    @Test
    public void testRegisterUser_UserAlreadyExists_ReturnsError() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        Map<String, String> result = authService.registerUser("testuser", email, "password");

        assertNotNull(result);
        assertEquals("User already exists. Please login.", result.get("error"));
    }

    @Test
    public void testRegisterUser_NewUser_ReturnsUserIdAndToken() {
        String email = "test@example.com";
        String username = "testuser";
        String password = "password";
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(1L, username, email, encodedPassword);
        String token = "mocked-jwt-token";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(jwtUtil.generateToken(email)).thenReturn(token);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L); // Simulate auto-generated ID
            return savedUser;
        });

        Map<String, String> result = authService.registerUser(username, email, password);

        assertNotNull(result);
        assertEquals("1", result.get("userId"));
        assertEquals(token, result.get("token"));
    }

    @Test
    public void testLoginUser_UserNotFound_ReturnsNull() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertNull(authService.loginUser(email, "password"));
    }

    @Test
    public void testLoginUser_InvalidPassword_ReturnsNull() {
        String email = "test@example.com";
        String password = "wrongpassword";
        String hashedPassword = passwordEncoder.encode("correctpassword");
        User user = new User(1L, "testuser", email, hashedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        assertNull(authService.loginUser(email, password));
    }

    @Test
    public void testLoginUser_ValidCredentials_ReturnsUserIdAndToken() {
        String email = "test@example.com";
        String password = "correctpassword";
        String hashedPassword = passwordEncoder.encode(password);
        String token = "mocked-jwt-token";
        User user = new User(1L, "testuser", email, hashedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(email)).thenReturn(token);

        Map<String, String> result = authService.loginUser(email, password);

        assertNotNull(result);
        assertEquals("1", result.get("userId"));
        assertEquals(token, result.get("token"));
    }
}
