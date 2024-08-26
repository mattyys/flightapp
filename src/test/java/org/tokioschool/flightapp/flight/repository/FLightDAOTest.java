package org.tokioschool.flightapp.flight.repository;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.tokioschool.flightapp.flight.domain.Airport;
import org.tokioschool.flightapp.flight.domain.Flight;
import org.tokioschool.flightapp.flight.domain.FlightImage;
import org.tokioschool.flightapp.flight.domain.FlightStatus;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@DataJpaTest(
    properties = {
      "spring.datasource.url+jdbc:h2:mem:testdb;MODE=MYSQL;DATABASE_TO_LOWER=TRUE",
      "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
      "spring.jpa.hibernate.ddl-auto=create-drop",
    })
class FLightDAOTest {

  @Autowired private AirportDAO airportDAO;
  @Autowired private FLightDAO fLightDAO;
  @Autowired private FlightImageDAO flightImageDAO;

  @BeforeEach
  void beforeEach() {
    Airport mad = Airport.builder().acronym("MAD").name("Barajas").country("Spain").build();
    Airport gla = Airport.builder().acronym("GLA").name("Glasgow").country("UK").build();

    airportDAO.save(mad);
    airportDAO.save(gla);

    Flight flight1 =
        Flight.builder()
            .occupancy(0)
            .capacity(90)
            .departure(mad)
            .arrival(gla)
            .number("IB1234")
            .departureTime(LocalDateTime.now().plusSeconds(60))
            .status(FlightStatus.SCHEDULED)
            .bookings(new HashSet<>())
            .build();

    FlightImage image =
        FlightImage.builder()
            .flight(flight1)
            .resourceId(UUID.randomUUID())
            .contentType("image/png")
            .size(20)
            .build();

    flight1.setImage(image);

    Flight flight2 =
        Flight.builder()
            .occupancy(0)
            .capacity(90)
            .departure(gla)
            .arrival(mad)
            .number("IB2235")
            .departureTime(LocalDateTime.now().plusSeconds(60))
            .status(FlightStatus.CANCELED)
            .bookings(new HashSet<>())
            .build();

    Flight flight3 =
        Flight.builder()
            .occupancy(0)
            .capacity(90)
            .departure(mad)
            .arrival(gla)
            .number("IB1234")
            .departureTime(LocalDateTime.now().minusSeconds(60))
            .status(FlightStatus.SCHEDULED)
            .bookings(new HashSet<>())
            .build();

    fLightDAO.saveAll(List.of(flight1, flight2, flight3));
  }

  @Test
  void givenFlights_whenFindByDepartureAcronym_thenReturnOk() {
    List<Flight> flightsMas = fLightDAO.findByDepartureAcronym("MAD");

    Assertions.assertEquals(2, flightsMas.size());

    List<Flight> flightGla = fLightDAO.findByDepartureAcronym("GLA");

    Assertions.assertEquals(1, flightGla.size());
  }

  @Test
  void givenFlights_whenFindNextFlights_thenReturnOk() {
    List<Flight> flights =
        fLightDAO.findByDepartureTimeIsAfterAndStatusIs(
            LocalDateTime.now(), FlightStatus.SCHEDULED);

    Assertions.assertEquals(1, flights.size());
    Assertions.assertNotNull(flights.getFirst().getImage());
  }

  @Test
  void givenFlights_whenFindByNumberLike_thenReturnOk() {
    // con % adelante y atras busca por cualquier parte de la cadena
    List<Flight> flightsLike = fLightDAO.findByNumberLike("%B12%");

    Assertions.assertEquals(2, flightsLike.size());

    // no es necesario poner % para buscar por una parte de la cadena
    List<Flight> flightsContains = fLightDAO.findByNumberContains("B12");

    Assertions.assertEquals(2, flightsContains.size());
  }

  // devolver resultados paginados
  @Test
  void givenFlights_whenGetAllPagesSorted_thenOk() {

    // EL PageRequest es el objeto que nos permite paginar y ordenar los resultados
    // le pasamos el numero de pagina y el tamaño de la pagina
    // el sort.by nos permite ordenar los resultados por el campo que le pasamos
    PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "departureTime"));
    Page<Flight> page1 = fLightDAO.findAll(pageRequest);

    Assertions.assertEquals(2, page1.getTotalPages()); // cantidad de paginas
    Assertions.assertEquals(3, page1.getTotalElements()); // cantidad de elementos
    Assertions.assertEquals(2, page1.getNumberOfElements()); // cantidad de elementos en la pagina
    Assertions.assertEquals(
        "IB2235", page1.getContent().getFirst().getNumber()); // el primer elemento de la pagina

    pageRequest = PageRequest.of(1, 2, Sort.by(Sort.Direction.DESC, "departureTime"));

    Page<Flight> page2 = fLightDAO.findAll(pageRequest);

    Assertions.assertEquals(
        1, page2.getNumberOfElements()); // cantidad de elementos en la segunda pagina
    Assertions.assertEquals(
        "IB1234",
        page2.getContent().getFirst().getNumber()); // el primer elemento de la segunda pagina
  }
}
