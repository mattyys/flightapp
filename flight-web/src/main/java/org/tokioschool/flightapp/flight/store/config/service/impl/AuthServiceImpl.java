package org.tokioschool.flightapp.flight.store.config.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.tokioschool.flightapp.flight.store.config.StoreConfigurationProperties;
import org.tokioschool.flightapp.flight.store.config.service.AuthService;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

  private final StoreConfigurationProperties storeConfigurationProperties;
  private final RestClient restClient = RestClient.builder().build();

  private String accessToken;
  private long expiresAt;

  @Override
  public String getAccessToken() {

    if (System.currentTimeMillis() < expiresAt) return accessToken;

    Map<String, String> authRequest =
        Map.of(
            "username",
            storeConfigurationProperties.username(),
            "password",
            storeConfigurationProperties.password());

    try {
      AuthResponseDTO authResponseDTO =
          restClient
              .post()
              .uri(storeConfigurationProperties.baseUrl() + "/store/spi/auth")
              .contentType(MediaType.APPLICATION_JSON)
              .body(authRequest)
              .retrieve()
              .body(AuthResponseDTO.class);

      accessToken = authResponseDTO.accessToken();
      expiresAt = authResponseDTO.getExpiresAt();

    } catch (Exception e) {
      log.error("Exception in file-store auth endpoint", e);
    }

    return accessToken;
  }

  private record AuthResponseDTO(String accessToken, int expiresIn) {
    public long getExpiresAt() {
      return System.currentTimeMillis() + expiresIn * 1000L - 5 * 1000L;
    }
  }
}
