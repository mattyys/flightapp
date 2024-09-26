package org.tokioschool.flightapp.base.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Profile(value = {"dev"})//Se ejecuta solo en el perfil dev o el perfil que se haya configurado
public class BaseLoggingService {

    //Se utiliza para obtener información sobre el entorno de ejecución de la aplicación en tiempo de ejecución.
    private final Environment environment;

    @Value("${application.custom:'no-value'}")
    private String custom;

    @PostConstruct
    void postConstruct() {

        log.info("BaseLoggingService, {} custom:{} profiles: [{}]",
            "info",
            custom,
            String.join("", environment.getActiveProfiles()));
        log.trace("BaseLoggingService, {}", "trace");
        log.debug("BaseLoggingService, {}", "debug");
        log.info("BaseLoggingService, {}", "info");
        log.warn("BaseLoggingService, {}", "warn");
        log.error("BaseLoggingService, {}", "error");

    }
}
