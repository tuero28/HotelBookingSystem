package com.example.demo.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.jwt")
@Getter @Setter
public class JwtProperties {
    private String secret;
    private long expirationMs = 86400000;       // 1 day
    private long refreshExpirationMs = 604800000; // 7 days
}