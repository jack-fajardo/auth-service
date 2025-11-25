package com.example.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.authservice.dto.LoginRequest;
import com.example.authservice.dto.LoginResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.authservice.dto.UserRequest;
import com.example.authservice.entity.User;
import com.example.authservice.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequest userRequest) {
        User created = userService.register(
                userRequest.getUsername(),
                userRequest.getEmail(),
                userRequest.getPasswordHash()
        );

        return ResponseEntity.ok("User registered with id: " + created.getId());
    }

    /**
     * Constructor injection for dependencies. - AuthenticationManager: used to
     * authenticate user credentials. - JwtService: custom service to generate
     * JWT tokens.
     */
    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /**
     * POST /auth/login Accepts username and password, authenticates the user,
     * and returns a JWT token if successful.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        // Step 1: Authenticate the user using Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Step 2: Generate a JWT token for the authenticated user
        String token = jwtService.generateToken(authentication);

        // Step 3: Return the token wrapped in a LoginResponse DTO
        return ResponseEntity.ok(new LoginResponse(token));
    }

}
