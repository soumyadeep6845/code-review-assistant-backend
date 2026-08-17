package com.example.personal.controllers;

import com.example.personal.kafka.CodeReviewEvent;
import com.example.personal.kafka.CodeReviewProducer;
import com.example.personal.models.CodeSubmission;
import com.example.personal.models.User;
import com.example.personal.models.dto.CodeSubmissionRequest;
import com.example.personal.repositories.CodeSubmissionRepository;
import com.example.personal.repositories.UserRepository;
import com.example.personal.security.JwtUtil;
import com.example.personal.services.AiCodeReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/code-review")
public class CodeReviewController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final CodeReviewProducer producer;
    private final CodeSubmissionRepository codeSubmissionRepository;


    @Autowired
    public CodeReviewController(
            JwtUtil jwtUtil,
            UserRepository userRepository,
            CodeReviewProducer producer,
            CodeSubmissionRepository codeSubmissionRepository) {

        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.producer = producer;
        this.codeSubmissionRepository = codeSubmissionRepository;
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitCode(@RequestHeader("Authorization") String token, @RequestBody CodeSubmissionRequest request) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Missing or invalid token"));
        }

        String jwt = token.replace("Bearer ", "");
        String email = jwtUtil.validateToken(jwt);

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token"));
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }

        User user = userOptional.get();

        CodeSubmission submission = CodeSubmission.builder()
                .userId(user.getId().toString())
                .code(request.getCode())
                .language(request.getLanguage())
                .status("PROCESSING")
                .submittedAt(LocalDateTime.now())
                .build();

        CodeSubmission savedSubmission = codeSubmissionRepository.save(submission);

        CodeReviewEvent event = new CodeReviewEvent(
                savedSubmission.getId(),
                user.getId().toString(),
                request.getCode(),
                request.getLanguage()
        );

        producer.sendCodeReviewRequest(event);

        return ResponseEntity.accepted().body(
                Map.of(
                        "message", "Code review request accepted",
                        "submissionId", savedSubmission.getId()
                )
        );
    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<?> getSubmission(
            @RequestHeader("Authorization") String token,
            @PathVariable Long submissionId) {

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Missing or invalid token"));
        }

        String jwt = token.replace("Bearer ", "");
        String email = jwtUtil.validateToken(jwt);

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid token"));
        }

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        User user = userOptional.get();

        Optional<CodeSubmission> submissionOptional =
                codeSubmissionRepository.findById(submissionId);

        if (submissionOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Submission not found"));
        }

        CodeSubmission submission = submissionOptional.get();

        // Make sure users can only access their own submissions
        if (!submission.getUserId().equals(user.getId().toString())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "error",
                            "You are not authorized to access this submission"
                    ));
        }

        return ResponseEntity.ok(submission);
    }

}
