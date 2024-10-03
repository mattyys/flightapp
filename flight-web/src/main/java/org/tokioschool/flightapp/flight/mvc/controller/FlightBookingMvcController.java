package org.tokioschool.flightapp.flight.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.tokioschool.flightapp.flight.dto.FlightBookingDTO;
import org.tokioschool.flightapp.flight.dto.FlightBookingSessionDTO;
import org.tokioschool.flightapp.flight.dto.FlightDTO;
import org.tokioschool.flightapp.flight.service.FlightBookingSessionService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@SessionAttributes(
    names = {"flightBookingSessionDTO"}) // se indica que se va a guardar en la sesion
public class FlightBookingMvcController {

  private final FlightBookingSessionService flightBookingSessionService;

    @ModelAttribute(
      name = "flightBookingSessionDTO") // hacemos referencia al nombre que se guardara en la sesion
  public FlightBookingSessionDTO
      flightBookingSessionDTO() { // el bean que creamos para guardar en la sesion
    return new FlightBookingSessionDTO();
  }

  @GetMapping("/flight/bookings-add/{flightId}")
  public RedirectView addBooking(
      @PathVariable("flightId") Long flightId,
      @ModelAttribute(value = "flightBookingSessionDTO")
          FlightBookingSessionDTO flightBookingSessionDTO) {

        // añadimos el vuelo seleccionado
    flightBookingSessionService.addFlightId(flightId, flightBookingSessionDTO);

    // redirigimos a la pagina de vuelos seleccionados
    return new RedirectView("/flight/bookings");
  }

  @GetMapping("/flight/bookings")
  public ModelAndView getBookings(
          @ModelAttribute(value = "flightBookingSessionDTO")
          FlightBookingSessionDTO flightBookingSessionDTO){

        // obtenemos los vuelos seleccionados
      Map<Long, FlightDTO> flightMap =
              flightBookingSessionService.getFlightsByIds(flightBookingSessionDTO);

      // mostramos la vista con los vuelos seleccionados
      ModelAndView modelAndView = new ModelAndView();
      modelAndView.addObject("flightBookingSession",flightBookingSessionDTO);
      modelAndView.addObject("flightMap",flightMap);
      modelAndView.setViewName("/flight/bookings/bookings-list");
      return modelAndView;
  }

  @PostMapping("/flight/bookings-confirm")
  public RedirectView confirmBooking(
          @ModelAttribute(value="flightBookingSessionDTO")
          FlightBookingSessionDTO flightBookingSessionDTO,
          SessionStatus sessionStatus){

        // confirmamos la reserva
      FlightBookingDTO flightBookingDTO =
              flightBookingSessionService.confirmFlightBookingSession(flightBookingSessionDTO);

      sessionStatus.setComplete();// limpiamos la sesion

      // redirigimos a la pagina de confirmacion con el identificador de la reserva
      return new RedirectView("/flight/bookings-confirm/%s".formatted(flightBookingDTO.getLocator()));
  }

  @GetMapping("/flight/bookings-confirm/{bookingLocator}")
    public ModelAndView confirmedBooking(
            @PathVariable("bookingLocator") String bookingLocator){

        // mostramos la vista de confirmacion
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("locator", bookingLocator);
        modelAndView.setViewName("/flight/bookings/bookings-confirm");
        return modelAndView;
  }


}
