package com.example.springsecurityjwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("hello")
    @Secured("USER")
    public ResponseEntity<String> helloUser() {
        return ResponseEntity.ok("Hello user");
    }
}
