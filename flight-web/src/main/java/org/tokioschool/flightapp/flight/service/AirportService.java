package org.tokioschool.flightapp.flight.service;


import org.tokioschool.flightapp.flight.dto.AirportDTO;

import java.util.List;

public interface AirportService {


    List<AirportDTO> getAirports();
}
