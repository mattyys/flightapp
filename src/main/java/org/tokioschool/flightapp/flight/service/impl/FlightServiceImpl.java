package org.tokioschool.flightapp.flight.service.impl;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.tokioschool.flightapp.flight.domain.Airport;
import org.tokioschool.flightapp.flight.domain.Flight;
import org.tokioschool.flightapp.flight.domain.FlightImage;
import org.tokioschool.flightapp.flight.domain.FlightStatus;
import org.tokioschool.flightapp.flight.dto.FlightDTO;
import org.tokioschool.flightapp.flight.mvc.dto.FlightMvcDTO;
import org.tokioschool.flightapp.flight.repository.AirportDAO;
import org.tokioschool.flightapp.flight.repository.FlightDAO;
import org.tokioschool.flightapp.flight.service.FlightImageService;
import org.tokioschool.flightapp.flight.service.FlightService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

  private final FlightDAO flightDAO;
  private final ModelMapper modelMapper;
  private final AirportDAO airportDAO;
  private final FlightImageService flightImageService;

  @Override
  public List<FlightDTO> getFlights() {

    return flightDAO.findAll().stream()
        .map(flight -> modelMapper.map(flight, FlightDTO.class))
        .toList();
  }

  @Override
  public FlightDTO getFlight(Long flightId) {
    return flightDAO
        .findById(flightId)
        .map(flight -> modelMapper.map(flight, FlightDTO.class))
        .orElseThrow(
            () -> new IllegalArgumentException("Flight with if:%s not found".formatted(flightId)));
  }

  @Override
  @Transactional
  public FlightDTO createFlight(FlightMvcDTO flightMvcDTO, @Nullable MultipartFile multipartFile) {

    Flight flight = createOrEdit(new Flight(), flightMvcDTO, multipartFile);
    return modelMapper.map(flight, FlightDTO.class);
  }

  @Override
  @Transactional // le indicamos a Spring que este metodo realiza una transaccion en la base de
                 // datos
  public FlightDTO edithFlight(FlightMvcDTO flightMvcDTO, @Nullable MultipartFile multipartFile) {
    Flight flight =
        flightDAO
            .findById(flightMvcDTO.getId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Flight with id:%s not found".formatted(flightMvcDTO.getId())));

    flight = createOrEdit(flight, flightMvcDTO, multipartFile);

    return modelMapper.map(flight, FlightDTO.class);
  }

  protected Flight
      createOrEdit( // los metodos usados en @Transactional deben ser protected(no privados)
      Flight flight, FlightMvcDTO flightMvcDTO, MultipartFile multipartFile) {

    Airport departure = getAirport(flightMvcDTO.getDeparture());
    Airport arrival = getAirport(flightMvcDTO.getArrival());

    FlightImage flightImage = flight.getImage();//si es edicion, recuperamos la imagen actual si no es nula

    if (!multipartFile.isEmpty()) {//si se ha subido una imagen se guarda
      flightImage= flightImageService.saveImage(multipartFile);
      flightImage.setFlight(flight);//agregamos el vuelo a la imagen para la relacion bidireccional de bd
    }

    flight.setCapacity(flightMvcDTO.getCapacity());
    flight.setDeparture(departure);
    flight.setArrival(arrival);
    flight.setStatus(FlightStatus.valueOf(flightMvcDTO.getStatus()));
    flight.setNumber(flightMvcDTO.getNumber());
    flight.setDepartureTime(flightMvcDTO.getDayTime());
    flight.setImage(flightImage);//se asigna la imagen al vuelo

    return flightDAO.save(flight);
  }

  protected Airport getAirport(String acronym) {
    return airportDAO
        .findByAcronym(acronym)
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "Airport with acronym:%s not found".formatted(acronym)));
  }
}


