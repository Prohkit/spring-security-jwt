package com.example.springsecurityjwt.security;

import com.example.springsecurityjwt.model.AuthenticationImpl;
import com.example.springsecurityjwt.model.User;
import com.example.springsecurityjwt.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TokenUtils {

    private final UserService userService;

    public AuthenticationImpl generate(Claims claims) {
        User user = userService.findByUsername(claims.getSubject());
        return new AuthenticationImpl(user);
    }
}
