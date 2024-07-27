package com.example.springsecurityjwt.exception.handler;

import com.example.springsecurityjwt.exception.AuthException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(AuthException.class)
    public ResponseEntity<String> authException(AuthException authException) {
        return ResponseEntity.badRequest().body(authException.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> expiredJwtException(ExpiredJwtException expiredJwtException) {
        return ResponseEntity.badRequest().body(expiredJwtException.getMessage());
    }
}
