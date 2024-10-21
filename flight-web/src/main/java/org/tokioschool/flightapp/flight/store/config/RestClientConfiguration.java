package org.tokioschool.flightapp.flight.store.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;
import org.tokioschool.flightapp.flight.store.config.service.AuthService;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class RestClientConfiguration {

  private final StoreConfigurationProperties storeConfigurationProperties;
  private final AuthService authService;

  // String token  = "Bearer " + authService.getAccessToken();

  @Bean
  public RestClient restClient() {
    return RestClient.builder()
        .baseUrl(storeConfigurationProperties.baseUrl())
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .requestInitializer(
            request ->
                request
                    .getHeaders()
                    .add(HttpHeaders.AUTHORIZATION, "Bearer " + authService.getAccessToken()))
        .build();
  }
}
