package com.example.personal.models;

import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testNoArgsConstructor() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    public void testAllArgsConstructor() {
        User user = new User(1L, "testUser", "test@example.com", "password123");
        assertEquals(Long.valueOf(1), user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
    }

    @Test
    public void testSettersAndGetters() {
        User user = new User();
        user.setId(2L);
        user.setUsername("newUser");
        user.setEmail("new@example.com");
        user.setPassword("newPassword");

        assertEquals(Long.valueOf(2), user.getId());
        assertEquals("newUser", user.getUsername());
        assertEquals("new@example.com", user.getEmail());
        assertEquals("newPassword", user.getPassword());
    }

    @Test
    public void testBuilder() {
        User user = User.builder()
                .id(3L)
                .username("builderUser")
                .email("builder@example.com")
                .password("builderPass")
                .build();

        assertEquals(Long.valueOf(3), user.getId());
        assertEquals("builderUser", user.getUsername());
        assertEquals("builder@example.com", user.getEmail());
        assertEquals("builderPass", user.getPassword());
    }
}
