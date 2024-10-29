package org.tokioschool.flightapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tokioschool.flightapp.domain.AirportRaw;
import org.tokioschool.flightapp.domain.AirportRawId;

@Repository
public interface AirportRawRepository extends JpaRepository<AirportRaw, AirportRawId> {}
