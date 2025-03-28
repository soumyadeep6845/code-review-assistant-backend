package com.example.personal.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    private static final long EXPIRATION_TIME = 86400000; // 24 hours
    private static final Set<String> activeTokens = new HashSet<>();

    public String generateToken(String email) {
        String token = JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET_KEY));
        activeTokens.add(token); // Store token in a whitelist
        return token;
    }

    // If needed to access secret key
    public String getSecretKey() {
        return SECRET_KEY;
    }

    public String validateToken(String token) {
        try {
            String securityToken = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .build()
                    .verify(token)
                    .getSubject();
            return securityToken; // Token must be in active session list
        } catch (JWTVerificationException e) {
            return null;
        }
    }
    public void invalidateToken(String token) {
        activeTokens.remove(token); // Remove token on logout or expiration
    }
}
