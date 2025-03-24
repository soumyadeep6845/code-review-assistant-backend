package com.example.personal.models.dto;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CodeSubmissionRequestTest {

    private CodeSubmissionRequest request;

    @Before
    public void setUp() {
        request = new CodeSubmissionRequest("user123", "System.out.println(\"Hello, World!\");", "Java");
    }

    @Test
    public void testGetUserId() {
        assertEquals("user123", request.getUserId());
    }

    @Test
    public void testSetUserId() {
        request.setUserId("newUser456");
        assertEquals("newUser456", request.getUserId());
    }

    @Test
    public void testGetCode() {
        assertEquals("System.out.println(\"Hello, World!\");", request.getCode());
    }

    @Test
    public void testSetCode() {
        request.setCode("print(\"Hello, Python!\")");
        assertEquals("print(\"Hello, Python!\")", request.getCode());
    }

    @Test
    public void testGetLanguage() {
        assertEquals("Java", request.getLanguage());
    }

    @Test
    public void testSetLanguage() {
        request.setLanguage("Python");
        assertEquals("Python", request.getLanguage());
    }

    @Test
    public void testToString() {
        String expected = "CodeSubmissionRequest{userId='user123', code='System.out.println(\"Hello, World!\");', language='Java'}";
        assertEquals(expected, request.toString());
    }
}
