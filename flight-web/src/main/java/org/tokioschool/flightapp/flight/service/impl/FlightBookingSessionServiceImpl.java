package org.tokioschool.flightapp.flight.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.tokioschool.flightapp.flight.dto.FlightBookingDTO;
import org.tokioschool.flightapp.flight.dto.FlightBookingSessionDTO;
import org.tokioschool.flightapp.flight.dto.FlightDTO;
import org.tokioschool.flightapp.flight.dto.UserDTO;
import org.tokioschool.flightapp.flight.service.FlightBookingService;
import org.tokioschool.flightapp.flight.service.FlightBookingSessionService;
import org.tokioschool.flightapp.flight.service.FlightService;
import org.tokioschool.flightapp.flight.service.UserService;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FlightBookingSessionServiceImpl implements FlightBookingSessionService {

  private final FlightService flightService;
  private final FlightBookingService flightBookingService;
  private final UserService userService;

  @Override
  public void addFlightId(Long flightId, FlightBookingSessionDTO flightBookingSessionDTO) {

    FlightDTO flightDTO = flightService.getFlight(flightId);
    Optional.ofNullable(flightBookingSessionDTO.getCurrentFlightId())
        .ifPresent(
            discardedFlightId ->
                flightBookingSessionDTO.getDiscardedFlightsIds().add(discardedFlightId));

    flightBookingSessionDTO.setCurrentFlightId(flightDTO.getId());
    flightBookingSessionDTO.getDiscardedFlightsIds().remove(flightDTO.getId());
  }

  @Override
  public FlightBookingDTO confirmFlightBookingSession(
      FlightBookingSessionDTO flightBookingSessionDTO) {
    FlightDTO flightDTO = flightService.getFlight(flightBookingSessionDTO.getCurrentFlightId());

    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    UserDTO userDTO =
        userService
            .findByEmail(username)
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "User with username:%s not found".formatted(username)));

    return flightBookingService.bookFlight(flightDTO.getId(), userDTO.getId());
  }

  @Override
  public Map<Long, FlightDTO> getFlightsByIds(FlightBookingSessionDTO flightBookingSessionDTO) {

    Set<Long> flightsIds = new HashSet<>(flightBookingSessionDTO.getDiscardedFlightsIds());
    Optional.ofNullable(flightBookingSessionDTO.getCurrentFlightId()).ifPresent(flightsIds::add);

    Map<Long, FlightDTO> flightMap = flightService.getFlightsById(flightsIds);

    for (Long flightId : flightsIds) {
      if (!flightMap.containsKey(flightId)) {
        throw new IllegalArgumentException("Flight with id:%s not found".formatted(flightId));
      }
    }
    return flightMap;
  }
}
