package com.example.personal.controllers;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.example.personal.models.dto.AuthRequest;
import com.example.personal.security.JwtUtil;
import com.example.personal.services.AuthService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Map;

public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testVerifyToken_ValidToken() {
        String token = "valid-token";
        String authHeader = "Bearer " + token;
        when(jwtUtil.validateToken(token)).thenReturn("user@example.com");

        ResponseEntity<String> response = authController.verifyToken(authHeader);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Valid token", response.getBody());
    }

    @Test
    public void testVerifyToken_InvalidToken() {
        String token = "invalid-token";
        String authHeader = "Bearer " + token;
        when(jwtUtil.validateToken(token)).thenReturn(null);

        ResponseEntity<String> response = authController.verifyToken(authHeader);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid token", response.getBody());
    }

    @Test
    public void testVerifyToken_MissingHeader() {
        ResponseEntity<String> response = authController.verifyToken(null);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unauthorized: No token provided", response.getBody());
    }

    @Test
    public void testRegister_Success() {
        AuthRequest request = new AuthRequest();
        when(authService.userExists(request.getEmail())).thenReturn(false);
        when(authService.registerUser(anyString(), anyString(), anyString()))
                .thenReturn(Map.of("userId", "123", "token", "jwt-token"));

        ResponseEntity<?> response = authController.register(request);
        Map<String, String> body = (Map<String, String>) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(body);
    }

    @Test
    public void testRegister_UserAlreadyExists() {
        AuthRequest request = new AuthRequest();
        when(authService.userExists(request.getEmail())).thenReturn(true);

        ResponseEntity<?> response = authController.register(request);
        Map<String, String> body = (Map<String, String>) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(body);
    }

    @Test
    public void testRegister_MissingFields() {
        AuthRequest request = new AuthRequest();

        ResponseEntity<?> response = authController.register(request);
        Map<String, String> body = (Map<String, String>) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(body);
    }

    @Test
    public void testLogin_Success() {
        AuthRequest request = new AuthRequest();
        when(authService.userExists(request.getEmail())).thenReturn(true);
        when(authService.loginUser(anyString(), anyString()))
                .thenReturn(Map.of("userId", "123", "token", "jwt-token"));

        ResponseEntity<?> response = authController.login(request);
        Map<String, String> body = (Map<String, String>) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(body);
    }

    @Test
    public void testLogin_UserNotFound() {
        AuthRequest request = new AuthRequest();
        when(authService.userExists(request.getEmail())).thenReturn(false);

        ResponseEntity<?> response = authController.login(request);
        Map<String, String> body = (Map<String, String>) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(body);
    }

    @Test
    public void testLogin_InvalidCredentials() {
        AuthRequest request = new AuthRequest();
        when(authService.userExists(request.getEmail())).thenReturn(true);
        when(authService.loginUser(anyString(), anyString())).thenReturn(null);

        ResponseEntity<?> response = authController.login(request);
        Map<String, String> body = (Map<String, String>) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(body);
    }

    @Test
    public void testLogin_MissingFields() {
        AuthRequest request = new AuthRequest();

        ResponseEntity<?> response = authController.login(request);
        Map<String, String> body = (Map<String, String>) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(body);
    }
}