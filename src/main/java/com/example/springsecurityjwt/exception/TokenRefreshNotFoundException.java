package com.example.springsecurityjwt.exception;

public class TokenRefreshNotFoundException extends RuntimeException {
    public TokenRefreshNotFoundException(String message) {
        super(message);
    }
}
