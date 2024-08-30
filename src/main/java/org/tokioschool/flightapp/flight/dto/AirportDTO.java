package org.tokioschool.flightapp.flight.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirportDTO {

  private String acronym;
  private String name;
  private String country;
}
