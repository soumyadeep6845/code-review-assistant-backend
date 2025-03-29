package com.example.personal.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.example.personal.models.CodeSubmission;
import com.example.personal.repositories.CodeSubmissionRepository;
import com.example.personal.services.AiCodeReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

public class AiCodeReviewServiceTest {

    @Mock
    private CodeSubmissionRepository repository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private AiCodeReviewService aiCodeReviewService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.defaultHeader(anyString(), anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        aiCodeReviewService = new AiCodeReviewService(repository, webClientBuilder);
    }

    @Test
    public void testAnalyzeCode_success() throws Exception {
        String aiResponseJson = "{\"choices\":[{\"message\":{\"content\":\"Refactor method foo().\"}}]}";
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(eq(HttpHeaders.AUTHORIZATION), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(Map.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(eq(String.class))).thenReturn(Mono.just(aiResponseJson));

        String result = aiCodeReviewService.analyzeCode("public void foo() {}", "Java");
        assertNotNull(result);
        assertEquals("Refactor method foo().", result);
    }

    @Test
    public void testAnalyzeCode_errorResponse() {
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(eq(HttpHeaders.AUTHORIZATION), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(Map.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    public void testSubmitCode() {
        String userId = "user123";
        String code = "public class Test {}";
        String language = "Java";
        String aiFeedback = "Sample AI Feedback";
        AiCodeReviewService spyService = spy(aiCodeReviewService);
        doReturn(aiFeedback).when(spyService).analyzeCode(code, language);
        CodeSubmission expectedSubmission = CodeSubmission.builder()
                .userId(userId)
                .code(code)
                .language(language)
                .aiFeedback(aiFeedback)
                .submittedAt(LocalDateTime.now())
                .build();
        when(repository.save(any(CodeSubmission.class))).thenReturn(expectedSubmission);
        CodeSubmission actualSubmission = spyService.submitCode(userId, code, language);

        assertNotNull(actualSubmission);
        assertEquals(userId, actualSubmission.getUserId());
        assertEquals(code, actualSubmission.getCode());
        assertEquals(language, actualSubmission.getLanguage());
        assertEquals(aiFeedback, actualSubmission.getAiFeedback());
        assertNotNull(actualSubmission.getSubmittedAt());
        verify(repository, times(1)).save(any(CodeSubmission.class));
        verify(spyService, times(1)).analyzeCode(code, language);
    }
}