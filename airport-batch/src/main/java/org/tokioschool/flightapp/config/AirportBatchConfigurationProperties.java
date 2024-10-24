package org.tokioschool.flightapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@ConfigurationProperties(prefix = "application.batch")
public record AirportBatchConfigurationProperties(Path basePath) {

  public Path getAirportsCsvPath() {
    return Path.of(basePath().toString(), "airports.csv");
  }

  public Path getExportPath() {
    return Paths.get(basePath().toString(), "exports", UUID.randomUUID() + ".json");
  }
}
