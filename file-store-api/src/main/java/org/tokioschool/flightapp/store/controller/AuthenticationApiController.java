package org.tokioschool.flightapp.store.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tokioschool.flightapp.store.dto.AuthenticatedMeResponseDTO;
import org.tokioschool.flightapp.store.dto.AuthenticationRequestDTO;
import org.tokioschool.flightapp.store.dto.AuthenticationResponseDTO;
import org.tokioschool.flightapp.store.service.AuthenticationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store/api/auth")
public class AuthenticationApiController {

  private final AuthenticationService authenticationService;

  @PostMapping
  public ResponseEntity<AuthenticationResponseDTO> postAuthenticate(
      @RequestBody AuthenticationRequestDTO authenticationRequestDTO) {

    return ResponseEntity.ok(authenticationService.authenticate(authenticationRequestDTO));
  }

  @GetMapping("/me")
  @SecurityRequirement(name="bearer-authentication")
  public ResponseEntity<AuthenticatedMeResponseDTO> getAuthenticated() {
    return ResponseEntity.ok(authenticationService.getAuthenticated());
  }
}
