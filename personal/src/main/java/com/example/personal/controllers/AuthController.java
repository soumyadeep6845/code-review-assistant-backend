package com.example.personal.controllers;

import com.example.personal.models.dto.AuthRequest;
import com.example.personal.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        if (request.getUsername() == null || request.getEmail() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing required fields"));
        }

        if (authService.userExists(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "User already exists"));
        }

        String response = authService.registerUser(request.getUsername(), request.getEmail(), request.getPassword());

        return ResponseEntity.ok(Map.of("message", response, "token", "sample-token")); // Send JSON response
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing required fields"));
        }

        if (!authService.userExists(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found. Please register."));
        }

        String token = authService.loginUser(request.getEmail(), request.getPassword());
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }

        return ResponseEntity.ok(Map.of("message", "Login successful", "token", token));
    }

}
