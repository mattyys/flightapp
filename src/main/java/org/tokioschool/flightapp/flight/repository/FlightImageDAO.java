package org.tokioschool.flightapp.flight.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tokioschool.flightapp.flight.domain.FlightImage;


@Repository
public interface FlightImageDAO extends CrudRepository<FlightImage, Long> {}
