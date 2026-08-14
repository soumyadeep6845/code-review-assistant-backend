package com.example.personal.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CodeReviewProducer {

    private final KafkaTemplate<String, CodeReviewEvent> kafkaTemplate;

    public CodeReviewProducer(KafkaTemplate<String, CodeReviewEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCodeReviewRequest(CodeReviewEvent event) {
        kafkaTemplate.send(
                KafkaTopics.CODE_REVIEW_REQUESTS,
                event.getUserId(),
                event
        );
    }
}