package org.tokioschool.flightapp.flight.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tokioschool.flightapp.flight.dto.FlightDTO;
import org.tokioschool.flightapp.flight.repository.AirportDAO;
import org.tokioschool.flightapp.flight.repository.FlightDAO;
import org.tokioschool.flightapp.flight.service.impl.FlightServiceImpl;



@ExtendWith(value = {SpringExtension.class})
@ContextConfiguration(
    classes = {FlightServiceSecurityTest.MockConfiguration.class, FlightServiceImpl.class})
@EnableMethodSecurity
public class FlightServiceSecurityTest {

  @SpyBean private FlightService flightService;

  @Test
  void givenNoUser_whenCreateFlight_thenAuthenticationMissingException() {

    Assertions.assertThatThrownBy(() -> flightService.createFlight(null, null))
        .isInstanceOf(AuthenticationCredentialsNotFoundException.class);
  }

  @Test
  @WithAnonymousUser
  void givenAnonymousUser_whenCreateFlight_thenAuthorizationDeniedException(){
    Assertions.assertThatThrownBy(() -> flightService.createFlight(null, null))
            .isInstanceOf(AuthorizationDeniedException.class);
  }

  @Test
  @WithMockUser(
          username = "username",
          authorities = {"USER"})
  void givenUser_whenCreateFlight_thenAuthorizationDeniedException(){
    Assertions.assertThatThrownBy(() -> flightService.createFlight(null, null))
            .isInstanceOf(AuthorizationDeniedException.class);
  }

  @Test
  @WithMockUser(
          username = "username",
          authorities = {"ADMIN"})
  void givenUser_whenCreateFlight_thenOk(){
    Mockito.doReturn(null).when(flightService).createFlight(Mockito.any(),Mockito.any());

    FlightDTO flightDTO = flightService.createFlight(null, null);

    Assertions.assertThat(flightDTO).isNull();

  }


  @Configuration
  public static class MockConfiguration {

    @Bean
    FlightDAO flightDAO() {
      return Mockito.mock(FlightDAO.class);
    }

    @Bean
    ModelMapper modelMapper() {
      return Mockito.mock(ModelMapper.class);
    }

    @Bean
    AirportDAO airportDAO() {
      return Mockito.mock(AirportDAO.class);
    }

    @Bean
    FlightImageService flightImageService() {
      return Mockito.mock(FlightImageService.class);
    }
  }
}
