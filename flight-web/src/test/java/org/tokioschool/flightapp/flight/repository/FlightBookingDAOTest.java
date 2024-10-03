package org.tokioschool.flightapp.flight.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.tokioschool.flightapp.flight.domain.Airport;
import org.tokioschool.flightapp.flight.domain.Flight;
import org.tokioschool.flightapp.flight.domain.FlightStatus;
import org.tokioschool.flightapp.flight.domain.User;
import org.tokioschool.flightapp.flight.dto.FlightBookingDTO;
import org.tokioschool.flightapp.flight.service.FlightBookingService;

import java.time.LocalDateTime;
import java.util.List;
//import java.util.concurrent.StructuredTaskScope;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
    properties = {
      "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MYSQL;DATABASE_TO_LOWER=TRUE",
      "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
      "spring.jpa.hibernate.ddl-auto=create-drop",
    })
class FlightBookingDAOTest {

  @Autowired private AirportDAO airportDAO;
  @Autowired private FlightDAO flightDAO;
  @Autowired private FlightBookingDAO flightBookingDAO;
  @Autowired private UserDAO userDAO;

  @Autowired private FlightBookingService flightBookingService;

  private Flight flight;
  private User user1;
  private User user2;

  @BeforeEach
  void beforeEach() {

    Airport mad = Airport.builder().acronym("MAD").name("Barajas").country("Spain").build();
    Airport gla = Airport.builder().acronym("GLA").name("Glasgow").country("UK").build();
    airportDAO.save(mad);
    airportDAO.save(gla);

    flight =
        Flight.builder()
            .occupancy(0)
            .capacity(1)
            .departure(mad)
            .arrival(gla)
            .number("IB1234")
            .departureTime(LocalDateTime.now().plusSeconds(60))
            .status(FlightStatus.SCHEDULED)
            .build();
    flightDAO.save(flight);

    user1 = User.builder().name("user1").surname("surname1").build();
    user2 = User.builder().name("user2").surname("surname2").build();

    userDAO.saveAll(List.of(user1, user2));
  }
/*
  @Test
  void givenConcurrentsUsers_whenBookingLastSlot_withVirtualThreads() throws Exception {
    User[] users = {user1, user2};

    try(StructuredTaskScope<FlightBookingDTO> flightBookingDTOStructuredTaskScope = new StructuredTaskScope<>()){

      // tarea 1
      StructuredTaskScope.Subtask<FlightBookingDTO> subtask0 =
          flightBookingDTOStructuredTaskScope.fork(
              () -> {
                return flightBookingService.bookFlight(flight.getId(), users[0].getId());
              });

      //tarea 2
        StructuredTaskScope.Subtask<FlightBookingDTO> subtask1 =
            flightBookingDTOStructuredTaskScope.fork(
                () -> {
                  return flightBookingService.bookFlight(flight.getId(), users[1].getId());
                });

        flightBookingDTOStructuredTaskScope.join();

        StructuredTaskScope.Subtask.State state0 = subtask0.state();
        StructuredTaskScope.Subtask.State state1 = subtask1.state();

        Assertions.assertThat(List.of(state0,state1)).containsExactlyInAnyOrder(
                StructuredTaskScope.Subtask.State.FAILED,
                StructuredTaskScope.Subtask.State.SUCCESS);

        //usando serializable test

//      if (state0 == StructuredTaskScope.Subtask.State.FAILED){
//        Throwable exception = subtask0.exception();
//        Assertions.assertThat(exception).isInstanceOf(CannotAcquireLockException.class);
//      }else if(state1 == StructuredTaskScope.Subtask.State.FAILED){
//        Throwable exception = subtask1.exception();
//        Assertions.assertThat(exception).isInstanceOf(CannotAcquireLockException.class);
//      }




        //usando optimistic locking
      if (state0 == StructuredTaskScope.Subtask.State.FAILED){
        Throwable exception = subtask0.exception();
        Assertions.assertThat(exception).isInstanceOf(OptimisticLockingFailureException.class);
      }else if(state1 == StructuredTaskScope.Subtask.State.FAILED){
        Throwable exception = subtask1.exception();
        Assertions.assertThat(exception).isInstanceOf(OptimisticLockingFailureException.class);
      }
    }
  }*/
}
