package org.tokioschool.flightapp.flight.mvc.dto;

import lombok.*;
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
    private  String number;
    private String departure;
    private String arrival;
    private LocalDateTime dayTime;

    @EnumValid(target = FlightDTO.Status.class, required = true)
    private String status;

    private Integer capacity;
}
