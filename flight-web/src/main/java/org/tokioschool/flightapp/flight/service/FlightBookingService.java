package org.tokioschool.flightapp.flight.service;

import org.tokioschool.flightapp.flight.dto.FlightBookingDTO;

public interface FlightBookingService {

    FlightBookingDTO bookFlight(Long flightId, String userId);
}
