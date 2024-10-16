package org.tokioschool.flightapp.flight.store.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.tokioschool.flightapp.flight.store.config.service.AuthService;

@Configuration
@RequiredArgsConstructor
public class RestClientConfiguration {

    private final StoreConfigurationProperties storeConfigurationProperties;
    private final AuthService authService;

    @Bean
    public RestClient restClient(){
        return RestClient.builder()
                .baseUrl(storeConfigurationProperties.baseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .requestInitializer(
                        request ->  request
                                .getHeaders()
                                .add(HttpHeaders.AUTHORIZATION, "Bearer " + authService.getAccessToken())
                )
                .build();
    }
}
