package com.example.authservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.authservice.dto.LoginRequest;
import com.example.authservice.dto.LoginResponse;
import com.example.authservice.dto.UserRequest;
import com.example.authservice.entity.User;
import com.example.authservice.service.JwtService;
import com.example.authservice.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(
            UserService userService,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            PasswordEncoder passwordEncoder
    ) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequest userRequest) {

        try {
            User created = userService.register(
                    userRequest.getUsername(),
                    userRequest.getEmail(),
                    userRequest.getPasswordHash()
            );
            return ResponseEntity.ok("User registered with id: " + created.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * POST /auth/login Accepts username and password, authenticates the user,
     * and returns a JWT token if successful.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        // Step 1: Retrieve user details (already throws if not found)
        UserDetails userDetails = userService.loadUserByUsername(request.getUsername());

        System.out.println("Raw password: " + request.getPassword());
        System.out.println("Stored hash: " + userDetails.getPassword());
        System.out.println("Matches: " + passwordEncoder.matches(request.getPassword(), userDetails.getPassword()));

        // Step 2: Authenticate
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Step 3: Generate JWT
        String token = jwtService.generateToken(authentication);

        // Step 4: Return JWT
        return ResponseEntity.ok(new LoginResponse(token));
    }

}
