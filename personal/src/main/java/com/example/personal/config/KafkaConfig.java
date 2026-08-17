package com.example.personal.config;

import com.example.personal.kafka.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic codeReviewTopic() {
        return TopicBuilder
                .name(KafkaTopics.CODE_REVIEW_REQUESTS)
                .partitions(3)
                .replicas(1)
                .build();
    }
}