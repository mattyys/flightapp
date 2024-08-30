package org.tokioschool.flightapp.flight.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.tokioschool.flightapp.core.converter.ModelMapperConfiguration;
import org.tokioschool.flightapp.flight.dto.AirportDTO;
import org.tokioschool.flightapp.flight.repository.AirportDAO;
import org.tokioschool.flightapp.flight.service.AirportService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

  private final AirportDAO airportDAO;
  private final ModelMapper modelMapper;

  @Override
  public List<AirportDTO> getAirports() {
    // con airportDAO.findAll() obtenemos todos los aeropuertos de la base de datos y para cada
    // elemento lo mapeamos a un AirportDTO con modelMapper.map(airport, AirportDTO.class)
    return airportDAO.findAll().stream()
        .map(airport -> modelMapper.map(airport, AirportDTO.class))
        .toList();
  }
}
