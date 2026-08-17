package com.example.personal.services;

import com.example.personal.models.CodeSubmission;
import com.example.personal.repositories.CodeSubmissionRepository;
import com.example.personal.util.CodeNormalizer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class AiCodeReviewService {

    private final WebClient webClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${openai.api.key}")  // Injecting API key here
    private String openAiApiKey;

    public AiCodeReviewService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String analyzeCode(String code, String language) {

        System.out.println("=== analyzeCode() STARTED ===");
        System.out.println("Language: " + language);

        // Normalize the code
        String normalizedCode = CodeNormalizer.normalize(code);

        // Generating a unique cache key based on the code and language
        String cacheKey = "code_review:" + language + ":" + normalizedCode.hashCode();

        // Checking if the response exists in Redis cache
        System.out.println("Checking Redis cache...");
        String cachedReview = redisTemplate.opsForValue().get(cacheKey);
        System.out.println("Redis cache check completed.");
        if (cachedReview != null) {
            System.out.println("Returning cached response for key: " + cacheKey);
            return cachedReview;
        }

        // Creating a JSON request body
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o-mini",
                "messages", new Object[]{
                        Map.of("role", "system", "content", "You are an expert code reviewer. Provide a detailed review."),
                        Map.of("role", "user", "content", "Analyze the following " + language +
                                " code and suggest improvements. Only provide the necessary improvements in short, don't explain anything. Answer directly in pointwise manner. Also, no need to provide the updated code again. Just the pointwise suggestions:\n\n" + code)
                },
                "max_tokens", 500
        );

        try {
            System.out.println("Calling OpenAI API...");
            String aiResponse = webClient.post()
                    .uri("/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + openAiApiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("OpenAI API response received.");

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(aiResponse);
            JsonNode choicesNode = rootNode.path("choices");

            if (choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode messageNode = choicesNode.get(0).path("message");
                JsonNode contentNode = messageNode.path("content");

                String extractedContent = contentNode.asText();
                System.out.println("Caching response for key: " + cacheKey);

                // Store the AI response in Redis with a 1-hour expiration
                redisTemplate.opsForValue().set(cacheKey, extractedContent, Duration.ofHours(1));

                return extractedContent;
            } else {
                return "Error: No valid content found in AI response.";
            }
        } catch (WebClientResponseException e) {
            System.out.println("=== OPENAI API ERROR ===");
            System.out.println("Status: " + e.getStatusCode());
            System.out.println("Response: " + e.getResponseBodyAsString());

            throw new RuntimeException(
                    "OpenAI API request failed: "
                            + e.getStatusCode()
                            + " - "
                            + e.getResponseBodyAsString(),
                    e
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {

            System.out.println("=== UNEXPECTED AI REVIEW ERROR ===");
            e.printStackTrace();

            throw e;
        }
    }
}
