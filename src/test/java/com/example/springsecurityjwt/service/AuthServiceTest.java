package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.exception.AuthException;
import com.example.springsecurityjwt.model.RefreshToken;
import com.example.springsecurityjwt.model.Role;
import com.example.springsecurityjwt.model.User;
import com.example.springsecurityjwt.model.dto.LoginRequest;
import com.example.springsecurityjwt.model.dto.RefreshTokenRequest;
import com.example.springsecurityjwt.model.dto.RegisterRequest;
import com.example.springsecurityjwt.model.dto.TokenResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private AccessTokenService accessTokenService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void register() {
        String username = "username";
        String password = "password";
        String email = "email";
        RegisterRequest request = new RegisterRequest(username, password, email);
        User user = new User(null, username, email, passwordEncoder.encode(password), true, Set.of(Role.USER));
        when(userService.existsByUsername(request.getUsername())).thenReturn(false);
        when(userService.existsByEmail(request.getEmail())).thenReturn(false);
        when(refreshTokenService.save(user.getId()))
                .thenReturn(new RefreshToken());

        authService.register(request);

        verify(userService).saveUser(user);
    }

    @Test
    void registerThrowsExceptionWhenUsernameAlreadyExists() {
        String username = "username";
        String password = "password";
        String email = "email";
        RegisterRequest request = new RegisterRequest(username, password, email);
        when(userService.existsByUsername(request.getUsername())).thenReturn(true);

        assertThrows(AuthException.class, () -> authService.register(request));
    }

    @Test
    void registerThrowsExceptionWhenEmailAlreadyExists() {
        String username = "username";
        String password = "password";
        String email = "email";
        RegisterRequest request = new RegisterRequest(username, password, email);
        when(userService.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(AuthException.class, () -> authService.register(request));
    }

    @Test
    void login() {
        String username = "username";
        String password = "password";
        String email = "email";
        LoginRequest loginRequest = new LoginRequest(username, password);
        User user = new User(null, username, email, passwordEncoder.encode(password), true, Set.of(Role.USER));

        when(userService.findByUsername(username)).thenReturn(user);
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("token");
        when(refreshTokenService.save(user.getId())).thenReturn(refreshToken);

        authService.login(loginRequest);

        verify(refreshTokenService).save(user.getId());
    }

    @Test
    void loginThrowExceptionWhenPasswordIncorrect() {
        String username = "username";
        String password = "password";
        String email = "email";
        LoginRequest loginRequest = new LoginRequest(username, password);
        User user = new User(null, username, email, passwordEncoder.encode(password), true, Set.of(Role.USER));

        when(userService.findByUsername(username)).thenReturn(user);
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(AuthException.class, () -> authService.login(loginRequest));
    }

    @Test
    void createTokenResponse() {
        String username = "username";
        String password = "password";
        String email = "email";

        User user = new User(null, username, email, passwordEncoder.encode(password), true, Set.of(Role.USER));
        RefreshToken refreshToken = new RefreshToken(null, user.getId(), "refreshToken", null);
        when(refreshTokenService.save(user.getId())).thenReturn(refreshToken);
        when(accessTokenService.generateToken(username, user.getId(), user.getRoles().stream().map(String::valueOf).toList()))
                .thenReturn("accessToken");

        TokenResponse result = authService.createTokenResponse(user);

        verify(accessTokenService).generateToken(username, user.getId(), user.getRoles().stream().map(String::valueOf).toList());
        verify(refreshTokenService).deleteByUserId(user.getId());
        verify(refreshTokenService).save(user.getId());
        assertEquals(new TokenResponse("accessToken", "refreshToken"), result);
    }

    @Test
    void refreshToken() {
        String token = "refreshToken";
        User user = new User();
        user.setId(1L);
        RefreshToken refreshToken = new RefreshToken(null, 1L, "refreshToken", null);
        RefreshTokenRequest request = new RefreshTokenRequest(token);
        when(refreshTokenService.findByToken(token)).thenReturn(refreshToken);
        when(userService.findById(refreshToken.getUserId())).thenReturn(user);
        when(refreshTokenService.save(user.getId())).thenReturn(refreshToken);

        authService.refreshToken(request);

        verify(refreshTokenService).findByToken(token);
        verify(userService).findById(refreshToken.getUserId());
    }
}