package org.tokioschool.flightapp.store.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class StoreApiSecurityConfiguration {

  @Bean
  public SecurityFilterChain configureApiConfiguration(HttpSecurity httpSecurity) throws Exception {

    return httpSecurity
        .securityMatcher("/store/api/**")
        .authorizeHttpRequests(
            authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/store/api/auth")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/store/api/resources")
                    .hasAuthority("write-resource")
                    .requestMatchers(HttpMethod.DELETE, "/store/api/resources/**")
                    .hasAuthority("write-resource")
                    .requestMatchers(HttpMethod.POST, "/store/api/resources/**")
                    .hasAnyAuthority("write-resource", "read-resource")
                    .requestMatchers("/store/api/**")
                    .authenticated())
        .oauth2ResourceServer(
            oauth2 ->
                oauth2.jwt(
                    jwtConfigurer ->
                        jwtConfigurer.jwtAuthenticationConverter(
                            new CustomJwtAuthenticationConverter())))
        .sessionManagement(
            httpSecuritySessionManagementConfigurer ->
                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS))
        .cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .build();
  }

  private static class CustomJwtAuthenticationConverter
      implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {

      JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
      jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
          jwt ->
              ((List<String>) jwt.getClaim("authorities"))
                  .stream().map(s -> (GrantedAuthority) new SimpleGrantedAuthority(s)).toList());

      return jwtAuthenticationConverter.convert(source);
    }
  }
}
