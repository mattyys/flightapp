package org.tokioschool.flightapp.flight.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class FlightBookingSessionDTO {

    private Long currentFlightId;
    private Set<Long> discardedFlightsIds = new HashSet<>();
}
