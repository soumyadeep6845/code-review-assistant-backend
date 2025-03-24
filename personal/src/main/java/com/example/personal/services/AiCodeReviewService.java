package com.example.personal.services;

import com.example.personal.models.CodeSubmission;
import com.example.personal.repositories.CodeSubmissionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class AiCodeReviewService {

    private final CodeSubmissionRepository repository;
    private final WebClient webClient;

    @Value("${openai.api.key}")  // Inject the API key here
    private String openAiApiKey;

    public AiCodeReviewService(CodeSubmissionRepository repository, WebClient.Builder webClientBuilder) {
        this.repository = repository;
        this.webClient = webClientBuilder
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String analyzeCode(String code, String language) {
        // Creating a JSON request body
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o-mini",
                "messages", new Object[]{
                        Map.of("role", "system", "content", "You are an expert code reviewer. Provide a detailed review."),
                        Map.of("role", "user", "content", "Analyze the following " + language + " code and suggest improvements. Only provide the necessary improvements in short, don't explain anything. Answer directly on point:\n\n" + code)
                },
                "max_tokens", 500
        );

        try {
            String aiResponse = webClient.post()
                    .uri("/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + openAiApiKey)  // API key applied here
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Parse JSON to extract the content field
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(aiResponse);
            JsonNode choicesNode = rootNode.path("choices");

            if (choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode messageNode = choicesNode.get(0).path("message");
                JsonNode contentNode = messageNode.path("content");

                String extractedContent = contentNode.asText();
                System.out.println(extractedContent);

                // Truncate response if it exceeds 65,000 characters
                return extractedContent.length() > 10 ? extractedContent.substring(0, 10) : extractedContent;

            } else {
                return "Error: No valid content found in AI response.";
            }
        } catch (WebClientResponseException e) {
            return "Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString();
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public CodeSubmission submitCode(String userId, String code, String language) {
        String aiFeedback = analyzeCode(code, language);

        CodeSubmission submission = CodeSubmission.builder()
                .userId(userId)
                .code(code)
                .language(language)
                .aiFeedback(aiFeedback)
                .submittedAt(LocalDateTime.now())
                .build();

        return repository.save(submission);
    }
}
