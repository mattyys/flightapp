package org.tokioschool.flightapp.store.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StoreConfigurationProperties.class)
@Slf4j
public class StoreConfiguration {

    @Value("${APPLICATION_CUSTOM}")
    private String applicationCustom;

    @PostConstruct
    public void init(){
      log.info("application-custom: {}", applicationCustom);
    }
}


