package org.tokioschool.flightapp.flight.service;


import jakarta.annotation.Nullable;
import org.springframework.web.multipart.MultipartFile;
import org.tokioschool.flightapp.flight.dto.FlightDTO;
import org.tokioschool.flightapp.flight.mvc.dto.FlightMvcDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FlightService {

    List<FlightDTO> getFlights();

    FlightDTO getFlight(Long flightId);

    FlightDTO createFlight(FlightMvcDTO flightMvcDTO, @Nullable MultipartFile multipartFile);

    FlightDTO editFlight(FlightMvcDTO flightMvcDTO, @Nullable MultipartFile multipartFile);

    Map<Long, FlightDTO> getFlightsById(Set<Long> flightsIds);
}


