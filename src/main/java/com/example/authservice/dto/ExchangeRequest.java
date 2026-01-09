package com.example.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for capturing exchange request data. This object is sent by the client
 * when attempting to log in.
 */
@Data // Lombok: generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Lombok: generates a no-argument constructor
@AllArgsConstructor // Lombok: generates a constructor with all fields
public class ExchangeRequest {

    /**
     * The one time code provided by the client. Used for exchanging into a
     * token
     *
     */
    private String oneTimeCode;
}
