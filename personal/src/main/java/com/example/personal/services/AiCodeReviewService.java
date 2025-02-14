package com.example.personal.services;

import com.example.personal.models.CodeSubmission;
import com.example.personal.repositories.CodeSubmissionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDateTime;

@Service
public class AiCodeReviewService {

    private final CodeSubmissionRepository repository;
    private final WebClient webClient;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    public AiCodeReviewService(CodeSubmissionRepository repository) {
        this.repository = repository;
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/completions")
                .defaultHeader("Authorization", "Bearer " + openAiApiKey)
                .build();
    }

    public String analyzeCode(String code, String language) {
        String prompt = "Analyze this " + language + " code and suggest improvements:\n" + code;

        String aiResponse = webClient.post()
                .bodyValue("{ \"model\": \"gpt-4\", \"prompt\": \"" + prompt + "\", \"max_tokens\": 150 }")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return aiResponse;
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
