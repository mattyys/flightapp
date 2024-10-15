package org.tokioschool.flightapp.store.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.tokioschool.flightapp.store.dto.AuthenticatedMeResponseDTO;
import org.tokioschool.flightapp.store.dto.AuthenticationRequestDTO;
import org.tokioschool.flightapp.store.dto.AuthenticationResponseDTO;
import org.tokioschool.flightapp.store.service.AuthenticationService;
import org.tokioschool.flightapp.store.service.JwtService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  @Override
  public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO) {

    Authentication authenticate =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authenticationRequestDTO.getUsername(), authenticationRequestDTO.getPassword()));

    UserDetails userDetails = (UserDetails) authenticate.getPrincipal();

    Jwt jwt = jwtService.generateToken(userDetails);

    return AuthenticationResponseDTO.builder()
        .accessToken(jwt.getTokenValue())
        .expiresIn(ChronoUnit.SECONDS.between(Instant.now(), jwt.getExpiresAt()) + 1)
        .build();
  }

  @Override
  public AuthenticatedMeResponseDTO getAuthenticated() {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    return AuthenticatedMeResponseDTO.builder()
            .username(authentication.getName())
            .authorities(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
            .build();
  }
}

