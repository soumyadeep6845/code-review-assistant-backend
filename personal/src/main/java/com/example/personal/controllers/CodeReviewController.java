package com.example.personal.controllers;

import com.example.personal.models.CodeSubmission;
import com.example.personal.models.dto.CodeSubmissionRequest;
import com.example.personal.services.AiCodeReviewService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/code-review")
public class CodeReviewController {

    private final AiCodeReviewService service;

    public CodeReviewController(AiCodeReviewService service) {
        this.service = service;
    }

    @PostMapping("/submit")
    public CodeSubmission submitCode(@RequestBody CodeSubmissionRequest request) {
        return service.submitCode(request.getUserId(), request.getCode(), request.getLanguage());
    }
}
