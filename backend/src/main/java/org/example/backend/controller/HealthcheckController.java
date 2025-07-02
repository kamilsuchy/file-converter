package org.example.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthcheckController {

    private final long startTime = System.currentTimeMillis();

    @GetMapping("/health")
    public ResponseEntity healthCheck() {

        return ResponseEntity.ok().build();
    }
}
