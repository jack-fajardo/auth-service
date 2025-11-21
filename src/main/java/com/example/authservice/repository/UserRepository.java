package com.example.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.authservice.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
