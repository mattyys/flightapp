package org.tokioschool.flightapp.flight.service;

import org.tokioschool.flightapp.flight.dto.FlightBookingDTO;
import org.tokioschool.flightapp.flight.dto.FlightBookingSessionDTO;
import org.tokioschool.flightapp.flight.dto.FlightDTO;

import java.util.Map;

public interface FlightBookingSessionService {

    void addFlightId(Long flightId, FlightBookingSessionDTO flightBookingSessionDTO);

    FlightBookingDTO confirmFlightBookingSession(FlightBookingSessionDTO flightBookingSessionDTO);

    Map<Long, FlightDTO> getFlightsByIds(FlightBookingSessionDTO flightBookingSessionDTO);

}

