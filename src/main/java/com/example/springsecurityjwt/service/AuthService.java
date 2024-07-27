package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.exception.AuthException;
import com.example.springsecurityjwt.model.RefreshToken;
import com.example.springsecurityjwt.model.Role;
import com.example.springsecurityjwt.model.User;
import com.example.springsecurityjwt.model.dto.LoginRequest;
import com.example.springsecurityjwt.model.dto.RefreshTokenRequest;
import com.example.springsecurityjwt.model.dto.RegisterRequest;
import com.example.springsecurityjwt.model.dto.TokenResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserService userService;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse register(RegisterRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            throw new AuthException("A user with that name already exists");
        }
        if (userService.existsByEmail(request.getEmail())) {
            throw new AuthException("A user with that email already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setAuthenticated(true);
        user.setRoles(Set.of(Role.USER));
        userService.saveUser(user);
        return createTokenResponse(user);
    }

    public TokenResponse login(LoginRequest request) {
        User user = userService.findByUsername(request.getUsername());
        user.setAuthenticated(true);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException("Incorrect password for user: " + request.getUsername());
        }
        return createTokenResponse(user);
    }

    public TokenResponse createTokenResponse(User user) {
        String accessToken = accessTokenService.generateToken(
                user.getUsername(),
                user.getId(),
                user.getRoles().stream().map(String::valueOf).toList()
        );
        refreshTokenService.deleteByUserId(user.getId());
        RefreshToken refreshToken = refreshTokenService.save(user.getId());
        return new TokenResponse(accessToken, refreshToken.getToken());
    }

    public TokenResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken());
        User user = userService.findById(refreshToken.getUserId());
        user.setAuthenticated(true);
        return createTokenResponse(user);
    }
}
