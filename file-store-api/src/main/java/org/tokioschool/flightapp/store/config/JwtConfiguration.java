package org.tokioschool.flightapp.store.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableConfigurationProperties(JwtConfigurationProperties.class)
@RequiredArgsConstructor
public class JwtConfiguration {

  private final JwtConfigurationProperties jwtConfigurationProperties;

  @Bean
  public AuthenticationManager authenticationManager(
          final AuthenticationConfiguration authenticationConfiguration) throws Exception{
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public NimbusJwtEncoder nimbusJwtEncoder() {
    return new NimbusJwtEncoder(
        new ImmutableSecret<>(
            new SecretKeySpec(jwtConfigurationProperties.secret().getBytes(), "HMAC")));

    // HMAC es un algoritmo de cifrado simétrico que se utiliza para firmar y verificar mensajes
    // digitales y no es reversible.
  }

  @Bean
  public NimbusJwtDecoder nimbusJwtDecoder() {
    return NimbusJwtDecoder.withSecretKey(new SecretKeySpec(jwtConfigurationProperties.secret().getBytes(), "HMAC"))
        .build();
  }

}

