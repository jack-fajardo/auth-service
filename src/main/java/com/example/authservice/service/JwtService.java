package com.example.authservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Service for handling JWT token generation. This class creates tokens after
 * successful authentication.
 */
@Service
public class JwtService {

    // Secret key used to sign the JWT.
    // In production, store this securely (e.g., environment variable).
    private final String SECRET_KEY = "your-secret-key";

    // Token validity period (e.g., 1 hour = 3600000 ms).
    private final long EXPIRATION_TIME = 1000 * 60 * 60;

    /**
     * Generates a JWT token for the authenticated user.
     *
     * @param authentication The Authentication object from Spring Security
     * @return A signed JWT token string
     */
    public String generateToken(Authentication authentication) {
        return Jwts.builder()
                // Subject = username of the authenticated user
                .setSubject(authentication.getName())
                // Issue time = now
                .setIssuedAt(new Date())
                // Expiration time = now + EXPIRATION_TIME
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                // Sign the token with HS256 algorithm and secret key
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
