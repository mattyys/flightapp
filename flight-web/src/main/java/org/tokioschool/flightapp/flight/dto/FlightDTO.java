package org.tokioschool.flightapp.flight.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightDTO {

  private Long id;
  private String number;
  private String
      departureAcronym; // el nombre hace referencia al acronym del Airport Departure nos facilita
                        // la busqueda del acronym
  private String arrivalAcronym;//idem anterior
  private LocalDateTime departureTime;
  private Status status;
  private Integer capacity;
  private Integer occupancy;
  private ResourceDTO image;

  public enum Status {
    SCHEDULED,
    CANCELED;
  }
}
