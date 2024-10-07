package org.tokioschool.flightapp.flight.store.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.store")
public record StoreConfigurationProperties(String baseUrl) {}
