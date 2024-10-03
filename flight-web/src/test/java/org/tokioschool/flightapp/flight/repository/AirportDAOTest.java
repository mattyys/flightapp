package org.tokioschool.flightapp.flight.repository;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.tokioschool.flightapp.flight.domain.Airport;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = {
        "spring.datasource.url+jdbc:h2:mem:testdb;MODE=MYSQL;DATABASE_TO_LOWER=TRUE",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
})
class AirportDAOTest {

  @Autowired private AirportDAO airportDAO;

  @BeforeEach
  void beforeEach() {
    Airport mad = Airport.builder().acronym("MAD").name("Barajas").country("Spain").build();

    Airport gla = Airport.builder().acronym("GLA").name("Glasgow").country("UK").build();

    airportDAO.save(mad);
    airportDAO.save(gla);
  }

    @Test
    void givenAirports_whenFindAll_thenReturnOk(){
      List<Airport> airports = airportDAO.findAll();

      Assertions.assertEquals(2, airports.size());
    }

    @Test
  void givenAirport_whenFindByAcronym_thenReturnOk(){

      Optional<Airport> maybeMad = airportDAO.findByAcronym("MAD");

      Assertions.assertTrue(maybeMad.isPresent());
      Assertions.assertNotNull(maybeMad.get().getAcronym());

    }



}
