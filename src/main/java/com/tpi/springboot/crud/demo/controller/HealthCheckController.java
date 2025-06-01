package com.tpi.springboot.crud.demo.controller;

import com.tpi.springboot.crud.demo.domain.HealthCheck;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/health-check")
    public ResponseEntity<HealthCheck> healthCheck() {
        HealthCheck response = new HealthCheck("Application is healthy and running.");

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public void hello() {
    }

}
