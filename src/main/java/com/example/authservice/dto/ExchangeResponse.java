package com.example.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for sending exchange response data back to the client. Typically contains
 * the JWT token or session info.
 */
@Data // Lombok: generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Lombok: generates a no-argument constructor
@AllArgsConstructor // Lombok: generates a constructor with all fields
public class ExchangeResponse {

    /**
     * The JWT token generated after successful authentication. The client will
     * use this token for subsequent requests.
     */
    private String token;
}
