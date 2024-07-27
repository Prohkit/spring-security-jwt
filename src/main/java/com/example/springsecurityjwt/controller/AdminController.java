package com.example.springsecurityjwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("hello")
    @Secured("ADMIN")
    public ResponseEntity<String> helloAdmin() {
        return ResponseEntity.ok("Hello admin");
    }
}
