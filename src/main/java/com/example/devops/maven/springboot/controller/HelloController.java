package com.example.devops.maven.springboot.controller;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class HelloController implements HealthIndicator {

    @GetMapping("/mtls/page")
    public String mtlsPage(HttpServletRequest request) {
        return "This is the mTLS secured page, accessed via: " + request.getRequestURL();
    }

    @GetMapping("/tls/page")
    public String tlsPage(HttpServletRequest request) {
        return "This is the TLS secured page, accessed via: " + request.getRequestURL();
    }

    @Override
    @GetMapping("/actuator/health")
    public Health health() {
        return Health.up().withDetail("message", "App is healthy!").build();
    }
}