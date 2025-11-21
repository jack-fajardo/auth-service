package com.example.authservice.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.authservice.entity.User;
import com.example.authservice.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User register(String username, String email, String rawPassword) {
        String hashed = passwordEncoder.encode(rawPassword);

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(hashed);
        user.setCreatedAt(java.time.Instant.now());

        return userRepository.save(user);
    }
}
