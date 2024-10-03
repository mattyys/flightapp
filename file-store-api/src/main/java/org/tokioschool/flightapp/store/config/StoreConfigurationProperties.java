package org.tokioschool.flightapp.flight.store.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;

@ConfigurationProperties(prefix = "application.store")
public record StoreConfigurationProperties(Path basePath) {

    //el path proporcionado en el archivo de configuración se concatena con el basePath
    public Path getPath(String file){
        return Path.of(basePath().toString(), file);
    }

}
