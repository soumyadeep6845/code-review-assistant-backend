package com.example.personal.controllers;

import com.example.personal.models.dto.AuthRequest;
import com.example.personal.security.JwtUtil;
import com.example.personal.services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;

    }
    @GetMapping("/verify")
    public ResponseEntity<String> verifyToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.error("Missing or invalid Authorization header");
            return ResponseEntity.status(401).body("Unauthorized: No token provided");
        }
        String token = authHeader.substring(7);
        logger.info("Received Token: " + token);


        String email = jwtUtil.validateToken(token);
        if (email != null) {
            logger.info("Token is valid");
            return ResponseEntity.ok("Valid token");
        } else {
            logger.error("Invalid token");
            return ResponseEntity.status(401).body("Invalid token");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        if (request.getUsername() == null || request.getEmail() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing required fields"));
        }

        if (authService.userExists(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "User already exists. Please login."));
        }

        Map<String, String> registerResponse = authService.registerUser(request.getUsername(), request.getEmail(), request.getPassword());

        return ResponseEntity.ok(Map.of(
                "message", "User registered successfully",
                "userId", registerResponse.get("userId"),
                "token", registerResponse.get("token")
        ));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing required fields"));
        }

        if (!authService.userExists(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found. Please register."));
        }

        Map<String, String> loginResponse = authService.loginUser(request.getEmail(), request.getPassword());
        if (loginResponse == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }

        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "userId", loginResponse.get("userId"),
                "token", loginResponse.get("token")
        ));
    }


}
