package org.tokioschool.flightapp.flight.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.tokioschool.flightapp.email.dto.EmailDTO;
import org.tokioschool.flightapp.email.service.EmailService;
import org.tokioschool.flightapp.flight.domain.Airport;
import org.tokioschool.flightapp.flight.domain.Flight;
import org.tokioschool.flightapp.flight.domain.FlightImage;
import org.tokioschool.flightapp.flight.domain.FlightStatus;
import org.tokioschool.flightapp.flight.dto.FlightImageResourceDTO;
import org.tokioschool.flightapp.flight.repository.AirportDAO;
import org.tokioschool.flightapp.flight.repository.FlightDAO;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
    properties = {
      "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MYSQL;DATABASE_TO_LOWER=TRUE",
      "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
      "spring.jpa.hibernate.ddl-auto=create-drop",
    })
class FlightEmailServiceITest {

  //es un mock para simular el servicio de imagenes
  @MockBean
  private FlightImageService flightImageService;

  //es un bean espiado para verificar que se llama al metodo sendEmail
  @SpyBean private EmailService emailService;

  @Autowired FlightEmailService flightEmailService;
  @Autowired private FlightDAO flightDAO;
  @Autowired private AirportDAO airportDAO;

  private Flight flight;

  @BeforeEach
  void beforeEach() {

    Airport bcn = Airport.builder().acronym("BCN").name("Barcelona").country("Spain").build();
    Airport lon = Airport.builder().acronym("LON").name("London").country("United Kingdom").build();
    airportDAO.save(bcn);
    airportDAO.save(lon);

    flight =
        Flight.builder()
            .occupancy(0)
            .capacity(100)
            .departure(bcn)
            .arrival(lon)
            .number("EZY1234")
            .departureTime(LocalDateTime.now().plusDays(25))
            .status(FlightStatus.SCHEDULED)
            .bookings(new HashSet<>())
            .build();

    flight.setImage(FlightImage.builder().resourceId(UUID.randomUUID()).flight(flight).build());
    flightDAO.save(flight);
  }

  @Test
  void givenFlight_whenSendingEmail_thenOk() throws Exception{

    InputStream flightTestResource = this.getClass().getResourceAsStream("/flight-test.jpg");

    FlightImageResourceDTO flightImageResourceDTO =
            FlightImageResourceDTO.builder()
                    .contentType("image/jpeg")
                    .filename("flight-test.jpg")
                    .content(flightTestResource.readAllBytes())
                    .build();

    Mockito.when(flightImageService.getImage(flight.getImage().getResourceId()))
            .thenReturn(flightImageResourceDTO);

    Mockito.doNothing().when(emailService).sendEmail(Mockito.any());

    flightEmailService.sendFlightEmail(flight.getId(), "matyalves@gmail.com");

    ArgumentCaptor<EmailDTO> emailDTOArgumentCaptor = ArgumentCaptor.forClass(EmailDTO.class);

    Mockito.verify(emailService, Mockito.times(1)).sendEmail(emailDTOArgumentCaptor.capture());

    Assertions.assertThat(emailDTOArgumentCaptor.getValue())
            .returns("matyalves@gmail.com", EmailDTO::getTo)
            .satisfies(emailDTO -> Assertions.assertThat(emailDTO.getAttachments()).hasSize(1));

  }

}
