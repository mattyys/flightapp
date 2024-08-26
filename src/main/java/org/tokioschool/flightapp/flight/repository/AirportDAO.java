package org.tokioschool.flightapp.flight.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tokioschool.flightapp.flight.domain.Airport;

@Repository
public interface AirportDAO extends JpaRepository<Airport, String> {

    Optional<Airport> findByAcronym(String id);

}
