package com.example.springsecurityjwt.configuration;

import com.example.springsecurityjwt.repository.UserRepository;
import com.example.springsecurityjwt.security.AuthTokenFilter;
import com.example.springsecurityjwt.security.TokenProvider;
import com.example.springsecurityjwt.security.TokenUtils;
import com.example.springsecurityjwt.service.UserService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {
    @Bean
    public BCryptPasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    public UserService userService(final UserRepository userRepository) {
        return new UserService(
                userRepository
        );
    }

    @Bean
    public TokenProvider tokenProvider() {
        return new TokenProvider();
    }

    @Bean
    public TokenUtils tokenUtils(UserService userService) {
        return new TokenUtils(userService);
    }

    @Bean
    public AuthTokenFilter authTokenFilter(TokenProvider tokenProvider, TokenUtils tokenUtils) {
        return new AuthTokenFilter(tokenProvider, tokenUtils);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(configurer ->
                        configurer.requestMatchers("/api/v1/auth/**").permitAll()
                                .anyRequest().authenticated())
                .sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
}
