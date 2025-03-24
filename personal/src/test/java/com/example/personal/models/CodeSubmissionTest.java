package com.example.personal.models;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class CodeSubmissionTest {

    private CodeSubmission codeSubmission;

    @Before
    public void setUp() {
        codeSubmission = new CodeSubmission();
    }

    @Test
    public void testNoArgsConstructor() {
        assertNotNull(codeSubmission);
    }

    @Test
    public void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        CodeSubmission submission = new CodeSubmission(1L, "user123", "print('Hello World')", "Python", "AI feedback", now);

        assertEquals(Long.valueOf(1L), submission.getId());
        assertEquals("user123", submission.getUserId());
        assertEquals("print('Hello World')", submission.getCode());
        assertEquals("Python", submission.getLanguage());
        assertEquals("AI feedback", submission.getAiFeedback());
        assertEquals(now, submission.getSubmittedAt());
    }

    @Test
    public void testSettersAndGetters() {
        LocalDateTime now = LocalDateTime.now();

        codeSubmission.setId(2L);
        codeSubmission.setUserId("user456");
        codeSubmission.setCode("System.out.println(\"Hello\");");
        codeSubmission.setLanguage("Java");
        codeSubmission.setAiFeedback("Great code!");
        codeSubmission.setSubmittedAt(now);

        assertEquals(Long.valueOf(2L), codeSubmission.getId());
        assertEquals("user456", codeSubmission.getUserId());
        assertEquals("System.out.println(\"Hello\");", codeSubmission.getCode());
        assertEquals("Java", codeSubmission.getLanguage());
        assertEquals("Great code!", codeSubmission.getAiFeedback());
        assertEquals(now, codeSubmission.getSubmittedAt());
    }

    @Test
    public void testBuilder() {
        LocalDateTime now = LocalDateTime.now();
        CodeSubmission submission = CodeSubmission.builder()
                .id(3L)
                .userId("user789")
                .code("console.log('Hello');")
                .language("JavaScript")
                .aiFeedback("Looks good!")
                .submittedAt(now)
                .build();

        assertEquals(Long.valueOf(3L), submission.getId());
        assertEquals("user789", submission.getUserId());
        assertEquals("console.log('Hello');", submission.getCode());
        assertEquals("JavaScript", submission.getLanguage());
        assertEquals("Looks good!", submission.getAiFeedback());
        assertEquals(now, submission.getSubmittedAt());
    }
}
