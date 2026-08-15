package com.example.telco_backend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public String test(Authentication authentication) {

        return "Autenticado como: " + authentication.getName()
                + " - Rol: " + authentication.getAuthorities();
    }

    @PostMapping
    public String testPost(Authentication authentication) {
        return "POST autenticado como: "
                + authentication.getName()
                + " - Rol: "
                + authentication.getAuthorities();
    }
}
