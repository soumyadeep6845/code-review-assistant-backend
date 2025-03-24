package com.example.personal.controllers;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.example.personal.models.CodeSubmission;
import com.example.personal.models.dto.CodeSubmissionRequest;
import com.example.personal.services.AiCodeReviewService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CodeReviewControllerTest {

    @Mock
    private AiCodeReviewService aiCodeReviewService;

    @InjectMocks
    private CodeReviewController codeReviewController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSubmitCode() {
        CodeSubmissionRequest request = new CodeSubmissionRequest(anyString(),anyString(),anyString());
        request.setUserId("user123");
        request.setCode("System.out.println(\"Hello, World!\");");
        request.setLanguage("Java");

        CodeSubmission expectedSubmission = new CodeSubmission();
        when(aiCodeReviewService.submitCode(request.getUserId(), request.getCode(), request.getLanguage()))
                .thenReturn(expectedSubmission);

        CodeSubmission actualSubmission = codeReviewController.submitCode(request);

        assertEquals(expectedSubmission, actualSubmission);
        verify(aiCodeReviewService, times(1)).submitCode(request.getUserId(), request.getCode(), request.getLanguage());
    }
}
