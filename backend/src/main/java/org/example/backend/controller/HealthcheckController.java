package org.example.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@RestController
public class HealthcheckController {

    private final long startTime = System.currentTimeMillis();
    private static final Logger logger = LoggerFactory.getLogger(HealthcheckController.class);

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {

        Instant start = Instant.now();
        logger.info("Healthcheck");
        Instant end = Instant.now();

        long durationMs = Duration.between(start, end).toMillis();

        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "startTime", startTime,
                "responseTimeMs", durationMs
        ));
    }
}
