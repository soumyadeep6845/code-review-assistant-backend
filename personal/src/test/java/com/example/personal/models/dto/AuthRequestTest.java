package com.example.personal.models.dto;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AuthRequestTest {
    private AuthRequest authRequest;

    @Before
    public void setUp() {
        authRequest = new AuthRequest();
    }

    @Test
    public void testSetAndGetUsername() {
        authRequest.setUsername("testUser");
        assertEquals("testUser", authRequest.getUsername());
    }

    @Test
    public void testSetAndGetEmail() {
        authRequest.setEmail("test@example.com");
        assertEquals("test@example.com", authRequest.getEmail());
    }

    @Test
    public void testSetAndGetPassword() {
        authRequest.setPassword("securePassword123");
        assertEquals("securePassword123", authRequest.getPassword());
    }
}
