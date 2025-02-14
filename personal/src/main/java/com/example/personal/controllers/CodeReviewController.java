package com.example.personal.controllers;

import com.example.personal.models.CodeSubmission;
import com.example.personal.services.AiCodeReviewService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/code-review")
public class CodeReviewController {

    private final AiCodeReviewService service;

    public CodeReviewController(AiCodeReviewService service) {
        this.service = service;
    }

    @PostMapping("/submit")
    public CodeSubmission submitCode(@RequestParam String userId, @RequestParam String code, @RequestParam String language) {
        return service.submitCode(userId, code, language);
    }
}
