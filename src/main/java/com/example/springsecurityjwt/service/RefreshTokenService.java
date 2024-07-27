package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.exception.TokenRefreshNotFoundException;
import com.example.springsecurityjwt.model.RefreshToken;
import com.example.springsecurityjwt.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenService {

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Value("${jwt.refreshTokenExpiration}")
    private Duration refreshTokenExpiration;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken save(Long userId) {
        String refreshTokenValue = UUID.randomUUID().toString();
        UUID id = UUID.randomUUID();
        RefreshToken refreshToken = new RefreshToken(id, userId, refreshTokenValue,
                new Date(System.currentTimeMillis() + refreshTokenExpiration.toMillis()).toInstant());

        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenRefreshNotFoundException("Refresh token not found"));
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
