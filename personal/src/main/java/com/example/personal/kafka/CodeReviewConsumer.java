package com.example.personal.kafka;

import com.example.personal.models.CodeSubmission;
import com.example.personal.repositories.CodeSubmissionRepository;
import com.example.personal.services.AiCodeReviewService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CodeReviewConsumer {

    private final AiCodeReviewService aiCodeReviewService;
    private final CodeSubmissionRepository codeSubmissionRepository;

    public CodeReviewConsumer(
            AiCodeReviewService aiCodeReviewService,
            CodeSubmissionRepository codeSubmissionRepository) {

        this.aiCodeReviewService = aiCodeReviewService;
        this.codeSubmissionRepository = codeSubmissionRepository;
    }

    @KafkaListener(
            topics = KafkaTopics.CODE_REVIEW_REQUESTS,
            groupId = "code-review-service"
    )
    public void consumeCodeReviewRequest(CodeReviewEvent event) {

        System.out.println("=== KAFKA MESSAGE RECEIVED ===");
        System.out.println("Event: " + event);

        try {

            System.out.println(
                    "Processing submission ID: "
                            + event.getSubmissionId()
            );

            // Find the submission created by the controller
            CodeSubmission submission =
                    codeSubmissionRepository
                            .findById(event.getSubmissionId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Submission not found: "
                                                    + event.getSubmissionId()
                                    )
                            );

            System.out.println(
                    "Found submission in database: "
                            + submission.getId()
            );

            // Perform AI analysis
            System.out.println("Starting AI code analysis...");

            String aiFeedback =
                    aiCodeReviewService.analyzeCode(
                            event.getCode(),
                            event.getLanguage()
                    );

            // Update the existing submission
            submission.setAiFeedback(aiFeedback);
            submission.setStatus("COMPLETED");

            codeSubmissionRepository.save(submission);

            System.out.println(
                    "Submission "
                            + submission.getId()
                            + " marked as COMPLETED."
            );

            System.out.println(
                    "=== AI CODE REVIEW PROCESSING COMPLETED ==="
            );

        } catch (Exception e) {

            System.out.println(
                    "=== ERROR PROCESSING KAFKA MESSAGE ==="
            );

            e.printStackTrace();

            // Try to mark the submission as FAILED
            try {

                codeSubmissionRepository
                        .findById(event.getSubmissionId())
                        .ifPresent(submission -> {

                            submission.setStatus("FAILED");

                            submission.setAiFeedback(
                                    "Code review processing failed."
                            );

                            codeSubmissionRepository.save(submission);

                            System.out.println(
                                    "Submission "
                                            + submission.getId()
                                            + " marked as FAILED."
                            );
                        });

            } catch (Exception databaseException) {

                System.out.println(
                        "=== ERROR UPDATING FAILED STATUS ==="
                );

                databaseException.printStackTrace();
            }
        }
    }
}