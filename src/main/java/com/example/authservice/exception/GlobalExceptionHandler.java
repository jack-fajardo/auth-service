package com.example.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> badCredentials(BadCredentialsException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Invalid username or password");
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiErrorResponse> disabled(DisabledException ex) {
        return build(HttpStatus.FORBIDDEN, "Account is disabled");
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ApiErrorResponse> locked(LockedException ex) {
        return build(HttpStatus.FORBIDDEN, "Account is locked");
    }

    // Optional: if you throw custom exceptions like InvalidExchangeCodeException
    // @ExceptionHandler(InvalidExchangeCodeException.class)
    // public ResponseEntity<ApiErrorResponse> invalidCode(InvalidExchangeCodeException ex) {
    //     return build(HttpStatus.UNAUTHORIZED, ex.getMessage());
    // }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> generic(Exception ex) {
        // You can log the exception here if you want
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(
                new ApiErrorResponse(message, status.value(), Instant.now())
        );
    }
}
