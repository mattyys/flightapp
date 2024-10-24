package org.tokioschool.flightapp.config;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AirportBatchConfigurationProperties.class)
public class AirportBatchConfiguration {}
