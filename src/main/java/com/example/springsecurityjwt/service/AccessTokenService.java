package com.example.springsecurityjwt.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.List;

@Service
public class AccessTokenService {

    private static final String ROLE_CLAIM = "role";

    private static final String ID_CLAIM = "id";

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.tokenExpiration}")
    private Duration tokenExpiration;

    public String generateToken(String username, Long id, List<String> roles) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date((new Date().getTime() + tokenExpiration.toMillis())))
                .claim(ROLE_CLAIM, roles)
                .claim(ID_CLAIM, id)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }


}
