package org.tokioschool.flightapp.config;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableConfigurationProperties(AirportBatchConfigurationProperties.class)
@EnableScheduling
public class AirportBatchConfiguration {}
