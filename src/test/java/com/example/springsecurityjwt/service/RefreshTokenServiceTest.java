package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.exception.TokenRefreshNotFoundException;
import com.example.springsecurityjwt.model.RefreshToken;
import com.example.springsecurityjwt.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        Duration refreshTokenExpiration = Duration.ofMillis(100_000);
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenExpiration", refreshTokenExpiration);
    }

    @Test
    void save() {
        Long userId = 1L;

        refreshTokenService.save(userId);

        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void findByToken() {
        String token = "token";
        RefreshToken refreshToken = new RefreshToken();
        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(refreshToken));

        refreshTokenService.findByToken(token);

        verify(refreshTokenRepository).findByToken(token);
    }

    @Test
    void findByTokenThrowsExceptionWhenTokenNotFound() {
        String token = "token";

        assertThrows(TokenRefreshNotFoundException.class, () -> refreshTokenService.findByToken(token));
    }

    @Test
    void deleteByUserId() {
        Long userId = 1L;

        refreshTokenService.deleteByUserId(userId);

        verify(refreshTokenRepository).deleteByUserId(userId);
    }
}