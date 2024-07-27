package com.example.springsecurityjwt.controller;

import com.example.springsecurityjwt.configuration.TestConfiguration;
import com.example.springsecurityjwt.model.dto.LoginRequest;
import com.example.springsecurityjwt.model.dto.RefreshTokenRequest;
import com.example.springsecurityjwt.model.dto.RegisterRequest;
import com.example.springsecurityjwt.model.dto.TokenResponse;
import com.example.springsecurityjwt.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(TestConfiguration.class)
class AuthControllerTest {

    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void register() {
        String username = "username";
        String password = "password";
        String email = "email";
        TokenResponse tokenResponse = new TokenResponse("accessToken", "refreshToken");
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        when(authService.register(registerRequest)).thenReturn(tokenResponse);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.access_token").value(tokenResponse.getAccessToken()))
                .andExpect(jsonPath("$.refresh_token").value(tokenResponse.getRefreshToken()));

        verify(authService).register(registerRequest);
    }

    @Test
    @SneakyThrows
    void login() {
        String username = "username";
        String password = "password";
        LoginRequest loginRequest = new LoginRequest(username, password);
        TokenResponse tokenResponse = new TokenResponse("accessToken", "refreshToken");
        when(authService.login(loginRequest)).thenReturn(tokenResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value(tokenResponse.getAccessToken()))
                .andExpect(jsonPath("$.refresh_token").value(tokenResponse.getRefreshToken()));

        verify(authService).login(loginRequest);
    }

    @Test
    @SneakyThrows
    void refreshToken() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("refreshTokenOld");
        TokenResponse tokenResponse = new TokenResponse("accessToken", "refreshToken");
        when(authService.refreshToken(refreshTokenRequest)).thenReturn(tokenResponse);

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value(tokenResponse.getAccessToken()))
                .andExpect(jsonPath("$.refresh_token").value(tokenResponse.getRefreshToken()));

        verify(authService).refreshToken(refreshTokenRequest);
    }
}