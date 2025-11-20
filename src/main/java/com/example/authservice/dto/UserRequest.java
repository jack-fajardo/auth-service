package com.example.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class UserRequest {

    private String username;
    private String email;
    private String passwordHash;
    private String role;
    private Boolean isActive;
    private String refreshToken;

}
