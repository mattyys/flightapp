package org.tokioschool.flightapp.flight.mvc.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.tokioschool.flightapp.core.validator.EnumValid;
import org.tokioschool.flightapp.flight.dto.FlightDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightMvcDTO {

  private Long id;

  @NotBlank(message = "{validation.flight.number_empty}") private String number;
  @NotBlank private String departure;
  @NotBlank private String arrival;

  @NotNull
  @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
  private LocalDateTime dayTime;

  @EnumValid(target = FlightDTO.Status.class, required = true,
  message = "{validation.flight.status.invalid}")//le idnidcamos que busque en messages el mensaje de validacion segun idioma
  private String status;

  @NotNull
  @Positive
  @Digits(fraction = 0, integer = 3)
  private Integer capacity;
}
