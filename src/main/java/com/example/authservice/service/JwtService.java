package com.example.authservice.service;

import java.security.Key;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Service for handling JWT token generation using a Base64-encoded secret key.
 */
@Service
public class JwtService {

    // Base64-encoded secret key (must be at least 256 bits for HS256)
    private static final String BASE64_SECRET_KEY = "bXktc3VwZXItc2VjcmV0LWtleS10aGF0LWlzLWxvbmctaGVyZQ==";
    // This is Base64 for "my-super-secret-key-that-is-long-here"

    private final Key key;

    // Token validity period (1 hour)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    public JwtService() {
        // Decode the Base64 secret and create HMAC key
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(BASE64_SECRET_KEY));
    }

    /**
     * Generates a signed JWT token for the authenticated user.
     *
     * @param authentication the Spring Security authentication object
     * @return a signed JWT string
     */
    public String generateToken(Authentication authentication) {
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
