package org.tokioschool.flightapp.store.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.tokioschool.flightapp.store.config.StoreConfigurationProperties;
import org.tokioschool.flightapp.store.dto.ResourceContentDTO;
import org.tokioschool.flightapp.store.dto.ResourceIdDTO;
import org.tokioschool.flightapp.store.service.StoreService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

class StoreServiceImplTest {

  private StoreService storeService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @TempDir private Path tempBasePath; // crea un directorio temporal para utilizar como path

  @BeforeEach
  void beforeEach() {

    StoreConfigurationProperties storeConfigurationProperties =
        new StoreConfigurationProperties(tempBasePath, List.of());

    storeService = new StoreServiceImpl(storeConfigurationProperties, objectMapper);
  }

  @Test
  void givenFIle_whenStored_thenOk() throws IOException {
    // simula un archivo se pasa nombre temporal, el nombre, el tipo de archivo y el contenido
    MockMultipartFile file =
        new MockMultipartFile(
            "file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

    Optional<ResourceIdDTO> resourceIdDTO = storeService.saveResource(file, "description");

    Path resourceContent = tempBasePath.resolve(resourceIdDTO.get().getResourceId().toString());
    ResourceContentDTO resourceDescription = objectMapper.readValue(
        tempBasePath.resolve(resourceIdDTO.get().getResourceId() + ".json").toFile(), ResourceContentDTO.class);

    Assertions.assertThat(resourceContent)
            .satisfies(path -> Assertions.assertThat(Files.exists(path)).isTrue())
            .satisfies(path -> Assertions.assertThat(Files.readAllBytes(path)).isEqualTo("Hello, World!".getBytes()));

    Assertions.assertThat(resourceDescription)
            .returns("hello.txt", ResourceContentDTO::getFilename)
            .returns("text/plain", ResourceContentDTO::getContentType)
            .returns("description", ResourceContentDTO::getDescription)
            .returns("Hello, World!".length(), ResourceContentDTO::getSize);
  }
}
