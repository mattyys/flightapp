package org.tokioschool.flightapp.store.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.tokioschool.flightapp.store.config.StoreConfigurationProperties;
import org.tokioschool.flightapp.store.domain.ResourceDescription;
import org.tokioschool.flightapp.store.dto.ResourceContentDTO;
import org.tokioschool.flightapp.store.dto.ResourceIdDTO;
import org.tokioschool.flightapp.store.service.StoreService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreServiceImpl implements StoreService {

  private final StoreConfigurationProperties storeConfigurationProperties;
  private final ObjectMapper objectMapper; // para convertir objetos a JSON y viceversa

  @Override
  public Optional<ResourceIdDTO> saveResource(MultipartFile multipartFile, String description) {

    // Se crea un objeto ResourceDescription con los datos del archivo
    ResourceDescription resourceDescription =
        ResourceDescription.builder()
            .description(description)
            .contentType(
                multipartFile.getContentType()) // multiparFile proviene de la peticion(frontend)
            .filename(multipartFile.getOriginalFilename())
            .size((int) multipartFile.getSize())
            .build();

    // Se crea un objeto ResourceIdDTO con un UUID aleatorio
    ResourceIdDTO resourceIdDTO = ResourceIdDTO.builder().resourceId(UUID.randomUUID()).build();

    // se obtiene la ruta donde se guardara el archivo
    Path pathToContent =
        storeConfigurationProperties.getPath(resourceIdDTO.getResourceId().toString());
    //resultado ejemplo:/tmp/123e4567-e89b-12d3-a456-426614174000


    // se obtiene la ruta donde se guardara la descripcion del archivo en formato JSON
    Path pathToDescription =
        storeConfigurationProperties.getPath(resourceIdDTO.getResourceId() + ".json");
    //resultado ejemplo:/tmp/123e4567-e89b-12d3-a456-426614174000.json

    try {
      Files.write(pathToContent, multipartFile.getBytes()); // se guarda el archivo

    } catch (IOException e) {
      log.error("Exception in saveResource", e);
      return Optional.empty();
    }

    try {
      objectMapper.writeValue(pathToDescription.toFile(), resourceDescription);
    } catch (IOException e) {
      // si hay un error y en el anterior try se guardo el archivo, se debe deshacer la operacion
      log.error("Exception in saveResource", e);
      return Optional.empty();
    }

    return Optional.of(
        resourceIdDTO); // si la operacion es correcta se retorna el objeto ResourceIdDTO(el
    // identificador)
  }

  @Override
  public Optional<ResourceContentDTO> findResource(UUID resourceId) {
    // se obtine la ruta del archivo guardado
    Path pathToContent = storeConfigurationProperties.getPath(resourceId.toString());
    // se obtiene la ruta del archivo JSON con la descripcion
    Path pathToDescription = storeConfigurationProperties.getPath(resourceId + ".json");

    // si no existe el archivo se retorna un Optional vacio
    if (!Files.exists(pathToContent)) {
      return Optional.empty();
    }

    byte[] bytes;

    try {
      // se lee el archivo y se guarda en un array de bytes
      bytes = Files.readAllBytes(pathToContent);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    ResourceDescription resourceDescription;
    try {
      // se lee el archivo JSON y se convierte a un objeto ResourceDescription
      resourceDescription =
          objectMapper.readValue(pathToDescription.toFile(), ResourceDescription.class);
    } catch (IOException e) {
      log.error("Exception in findResource", e);
      return Optional.empty();
    }

    // se retorna un objeto ResourceContentDTO con los datos del archivo
    return Optional.of(
        ResourceContentDTO.builder()
            .resourceId(resourceId)
            .content(bytes)
            .contentType(resourceDescription.getContentType())
            .filename(resourceDescription.getFilename())
            .size(resourceDescription.getSize())
            .build());
  }

  @Override
  public void deleteResource(UUID resourceId) {

    // se obtine la ruta del archivo guardado
    Path pathToContent = storeConfigurationProperties.getPath(resourceId.toString());
    // se obtiene la ruta del archivo JSON con la descripcion
    Path pathToDescription = storeConfigurationProperties.getPath(resourceId + ".json");

    try {
      Files.deleteIfExists(pathToContent); // se elimina el archivo
      Files.deleteIfExists(pathToDescription); // se elimina el archivo JSON
    } catch (IOException e) {
      log.error("Exception in deleteResource", e);
    }
  }
}
