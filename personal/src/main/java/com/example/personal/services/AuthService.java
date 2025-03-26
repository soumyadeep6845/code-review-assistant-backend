package com.example.personal.services;

import com.example.personal.models.User;
import com.example.personal.repositories.UserRepository;
import com.example.personal.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    /** Checking if a user already exists */
    public boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /** Registering a new user (with check to prevent duplicate registration) */
    public String registerUser(String username, String email, String password) {
        if (userExists(email)) {
            return "User already exists"; // Avoid throwing exception
        }
        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(null, username, email, hashedPassword);
        userRepository.save(user);
        return "User registered successfully";
    }

    /** Login (existing user) */
    public String loginUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return null; // User not found
        }

        User user = userOptional.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return null; // Invalid password
        }

        return jwtUtil.generateToken(email); // Return JWT token
    }
}
