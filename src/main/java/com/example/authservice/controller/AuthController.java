package com.example.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.authservice.dto.UserRequest;
import com.example.authservice.entity.User;
import com.example.authservice.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

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
}
