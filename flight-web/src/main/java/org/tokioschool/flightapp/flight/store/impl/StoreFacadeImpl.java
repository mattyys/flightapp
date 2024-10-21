package org.tokioschool.flightapp.flight.store.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.tokioschool.flightapp.flight.store.StoreFacade;
import org.tokioschool.flightapp.flight.store.config.service.AuthService;
import org.tokioschool.flightapp.flight.store.dto.ResourceContentDTO;
import org.tokioschool.flightapp.flight.store.dto.ResourceIdDTO;

import javax.print.DocFlavor;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreFacadeImpl implements StoreFacade {

  // private final RestTemplate restTemplate;
  private final RestClient restClient;
  private final ObjectMapper objectMapper;
  private final AuthService authService;

  @Override
  public Optional<ResourceIdDTO> saveResource(
      MultipartFile multipartFile, @Nullable String description) {

    HashMap<String, String> descriptionMap = new HashMap<>();
    descriptionMap.put("description", description);
    String descriptionBody;

    try {
      descriptionBody = objectMapper.writeValueAsString(descriptionMap);
    } catch (JsonProcessingException e) {
      descriptionBody = "";
      log.error("Error in saveResource - descriptionBody", e);
    }

    MediaType mediaType;
    try {
      mediaType = MediaType.parseMediaType(multipartFile.getContentType());
    } catch (InvalidMediaTypeException e) {
      mediaType = MediaType.APPLICATION_OCTET_STREAM;
      log.error("Error in saveResource - mediaType", e);
    }
    try {
      MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

      HttpHeaders descriptionHeaders = new HttpHeaders();
      descriptionHeaders.setContentType(MediaType.APPLICATION_JSON);
      parts.add("description", new HttpEntity<>(descriptionBody, descriptionHeaders));

      HttpHeaders contentHeaders = new HttpHeaders();
      contentHeaders.setContentType(mediaType);
      parts.add("content", new HttpEntity<>(multipartFile.getResource(), contentHeaders));

      ResourceIdDTO resourceIdDTO =
          restClient
              .post()
              .uri("/store/api/resources")
              .contentType(MediaType.MULTIPART_FORM_DATA)
              .body(parts)
              .retrieve()
              .body(ResourceIdDTO.class);

      return Optional.ofNullable(resourceIdDTO);
    } catch (Exception e) {
      log.error("Error in saveResource", e);
    }

    return Optional.empty();
  }

  @Override
  public Optional<ResourceContentDTO> findResource(UUID resourceId) {

    try {


      ResourceContentDTO resourceContentDTO =
          restClient
              .get()
              .uri("/store/api/resources/{resourceId}", resourceId)
              .header("Authorization", "Bearer " + authService.getAccessToken())
              .retrieve()
              .body(ResourceContentDTO.class);

      //            ResourceContentDTO resourceContentDTO = restTemplate.getForObject(
      //                    "http://localhost:8081/store/api/resources/{resourceId}",
      //                    ResourceContentDTO.class,
      //                    resourceId
      //            );

      return Optional.ofNullable(resourceContentDTO);

    } catch (Exception e) {
      log.error("Exception in finResource", e);
    }

    return Optional.empty();
  }

  @Override
  public void deleteResource(UUID resourceId) {

    restClient
        .delete()
        .uri("/store/api/resources/{resourceId}", resourceId)
        .retrieve()
        .toBodilessEntity();
  }
}
