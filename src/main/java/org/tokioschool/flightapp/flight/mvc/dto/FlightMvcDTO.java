package org.tokioschool.flightapp.flight.mvc.dto;

import lombok.*;

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
    private String status;
    private Integer capacity;
}
