package org.tokioschool.flightapp.flight.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.tokioschool.flightapp.flight.dto.FlightImageResourceDTO;
import org.tokioschool.flightapp.flight.service.FlightImageService;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class FlightResourceMvcController {

  private final FlightImageService flightImageService;

  @GetMapping("/flight/resources/{resourceId}")
  public ResponseEntity<byte[]> getFlightResourceAsStream(
      @PathVariable(value = "resourceId") UUID resourceId) {

    FlightImageResourceDTO flightImageResourceDTO = flightImageService.getIMage(resourceId);

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(flightImageResourceDTO.getContentType()))
        .contentLength(flightImageResourceDTO.getSize())
        .body(flightImageResourceDTO.getContent());
  }
}
