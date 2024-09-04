package org.tokioschool.flightapp.flight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tokioschool.flightapp.flight.domain.FlightBooking;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FlightBookingDAO extends JpaRepository<FlightBooking, Long> {

    List<FlightBooking> getFlightBookingsByFlightId(Long flightId);

    Optional<FlightBooking> findFlightBookingByLocator(UUID bookingLocator);

}
