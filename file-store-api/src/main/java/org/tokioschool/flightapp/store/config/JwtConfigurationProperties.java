package org.tokioschool.flightapp.store.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "application.jwt")
public record JwtConfigurationProperties(String secret, Duration duration) {}
