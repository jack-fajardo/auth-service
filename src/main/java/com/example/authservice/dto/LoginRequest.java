package com.example.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for capturing login request data. This object is sent by the client when
 * attempting to log in.
 */
@Data // Lombok: generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Lombok: generates a no-argument constructor
@AllArgsConstructor // Lombok: generates a constructor with all fields
public class LoginRequest {

    /**
     * The username provided by the client. Used to identify the user in the
     * system...
     */
    private String username;

    /**
     * The password provided by the client. Used to authenticate the user. NOTE:
     * This should never be exposed in responses.
     */
    private String password;
}
