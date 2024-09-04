package org.tokioschool.flightapp.flight.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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

  @GetMapping("/flight/downloads/{resourceId}")
  public ResponseEntity<byte[]> getFlightResourceAsAttachment(
      @PathVariable(value = "resourceId") UUID resourceId) {

    FlightImageResourceDTO flightImageResourceDTO = flightImageService.getIMage(resourceId);

    // instanciamos un objeto HttpHeaders para añadir los headers necesarios
    HttpHeaders httpHeaders = new HttpHeaders();

    // se indica el nombre del archivo que se va a descargar y se añade al header
    final String filename = "attachment; filename=" + flightImageResourceDTO.getFilename();

    httpHeaders.add(
        HttpHeaders.CONTENT_DISPOSITION, filename); // se añade el header con el nombre del archivo

    httpHeaders.add(
        HttpHeaders.CONTENT_TYPE,
        flightImageResourceDTO.getContentType()); // se añade el header con el tipo de contenido

    httpHeaders.add(
        HttpHeaders.CONTENT_LENGTH,
        String.valueOf(
            flightImageResourceDTO.getSize())); // se añade el header con el tamaño del archivo

    return ResponseEntity.ok()
        .headers(httpHeaders)
        .body(flightImageResourceDTO.getContent()); // se devuelve el archivo
  }
}
