package org.tokioschool.flightapp.flight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tokioschool.flightapp.flight.domain.FlightBooking;

@Repository
public interface FlightBookingDAO extends JpaRepository<FlightBooking, Long> {}
