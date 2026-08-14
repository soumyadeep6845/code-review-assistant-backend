package com.example.personal.kafka;

import com.example.personal.services.AiCodeReviewService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CodeReviewConsumer {

    private final AiCodeReviewService aiCodeReviewService;

    public CodeReviewConsumer(AiCodeReviewService aiCodeReviewService) {
        this.aiCodeReviewService = aiCodeReviewService;
    }

    @KafkaListener(
            topics = KafkaTopics.CODE_REVIEW_REQUESTS,
            groupId = "code-review-service"
    )
    public void consumeCodeReviewRequest(CodeReviewEvent event) {

        System.out.println("Received code review request from Kafka: " + event);

        aiCodeReviewService.submitCode(
                event.getUserId(),
                event.getCode(),
                event.getLanguage()
        );
    }
}