package com.example.personal.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    @Mock
    private Logger logger;

    private static final String SECRET_KEY = "test_secret";
    private static final String TEST_EMAIL = "user@example.com";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        jwtUtil = new JwtUtil();

        // Using reflection to set the private field SECRET_KEY
        try {
            java.lang.reflect.Field secretKeyField = JwtUtil.class.getDeclaredField("SECRET_KEY");
            secretKeyField.setAccessible(true);
            secretKeyField.set(jwtUtil, SECRET_KEY);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set SECRET_KEY via reflection", e);
        }
    }

    @Test
    public void testGenerateToken() {
        String token = jwtUtil.generateToken(TEST_EMAIL);
        assertNotNull(token);
        assertTrue(token.length() > 10); // Ensure token is not empty
    }

    @Test
    public void testValidateToken_Valid() {
        String token = jwtUtil.generateToken(TEST_EMAIL);
        String result = jwtUtil.validateToken(token);
        assertEquals(TEST_EMAIL, result);
    }

    @Test
    public void testValidateToken_Invalid() {
        String result = jwtUtil.validateToken("invalid_token");
        assertNull(result);
    }

    @Test
    public void testInvalidateToken() {
        String token = jwtUtil.generateToken(TEST_EMAIL);
        jwtUtil.invalidateToken(token);
        String result = jwtUtil.validateToken(token);
        assertNull(result);
    }

    @Test
    public void testGetSecretKey() {
        assertEquals(SECRET_KEY, jwtUtil.getSecretKey());
    }
}
