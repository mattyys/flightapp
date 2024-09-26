package org.tokioschool.flightapp.flight.service;

public interface FlightEmailService {
    void sendFlightEmail(Long flightId, String to);
}
