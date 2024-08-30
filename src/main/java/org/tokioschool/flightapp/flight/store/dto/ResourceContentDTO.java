package org.tokioschool.flightapp.flight.store.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized // Jacksonized es una anotación de Lombok que genera un constructor con todos los
             // argumentos y un método toString, equals y hashCode.
public class ResourceContentDTO {

  UUID resourceId;
  byte[] content;
  String contentType;
  String filename;
  String description;
  int size;
}
