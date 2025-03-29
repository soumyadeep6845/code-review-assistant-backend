package com.example.personal.controllers;

import com.example.personal.models.CodeSubmission;
import com.example.personal.models.User;
import com.example.personal.models.dto.CodeSubmissionRequest;
import com.example.personal.repositories.UserRepository;
import com.example.personal.security.JwtUtil;
import com.example.personal.services.AiCodeReviewService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;

public class CodeReviewControllerTest {

    @Mock
    private AiCodeReviewService service;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CodeReviewController controller;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSubmitCode_MissingToken() {
        ResponseEntity<?> response = controller.submitCode(null, new CodeSubmissionRequest("code", "System.out", "java"));
        assertEquals(FORBIDDEN, response.getStatusCode());
        assertEquals("Missing or invalid token", ((Map<?, ?>) response.getBody()).get("error"));
    }

    @Test
    public void testSubmitCode_InvalidTokenFormat() {
        ResponseEntity<?> response = controller.submitCode("InvalidToken", new CodeSubmissionRequest("code", "System.out", "java"));
        assertEquals(FORBIDDEN, response.getStatusCode());
        assertEquals("Missing or invalid token", ((Map<?, ?>) response.getBody()).get("error"));
    }

    @Test
    public void testSubmitCode_InvalidToken() {
        when(jwtUtil.validateToken("valid.jwt.token")).thenReturn(null);
        ResponseEntity<?> response = controller.submitCode("Bearer valid.jwt.token", new CodeSubmissionRequest("code", "System.out", "java"));
        assertEquals(UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid token", ((Map<?, ?>) response.getBody()).get("error"));
    }

    @Test
    public void testSubmitCode_UserNotFound() {
        when(jwtUtil.validateToken("valid.jwt.token")).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.submitCode("Bearer valid.jwt.token", new CodeSubmissionRequest("code", "System.out", "java"));
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", ((Map<?, ?>) response.getBody()).get("error"));
    }

    @Test
    public void testSubmitCode_SuccessfulSubmission() {
        User mockUser = new User();
        mockUser.setId(1L);

        CodeSubmission mockSubmission = new CodeSubmission();

        when(jwtUtil.validateToken("valid.jwt.token")).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(mockUser));
        when(service.submitCode("1", "code", "java")).thenReturn(mockSubmission);

        ResponseEntity<?> response = controller.submitCode("Bearer valid.jwt.token", new CodeSubmissionRequest("code", "System.out", "java"));
        assertEquals(OK, response.getStatusCode());
    }
}
