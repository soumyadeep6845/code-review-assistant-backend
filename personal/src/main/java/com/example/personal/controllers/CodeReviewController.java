package com.example.personal.controllers;

import com.example.personal.models.CodeSubmission;
import com.example.personal.models.User;
import com.example.personal.models.dto.CodeSubmissionRequest;
import com.example.personal.repositories.UserRepository;
import com.example.personal.security.JwtUtil;
import com.example.personal.services.AiCodeReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/code-review")
public class CodeReviewController {

    private final AiCodeReviewService service;
    private final JwtUtil jwtUtil; // Inject JwtUtil
    private final UserRepository userRepository;


    @Autowired
    public CodeReviewController(AiCodeReviewService service, JwtUtil jwtUtil, UserRepository userRepository) {
        this.service = service;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
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
        CodeSubmission submission = service.submitCode(user.getId().toString(), request.getCode(), request.getLanguage());
        return ResponseEntity.ok(submission);
    }

}
