package org.tokioschool.flightapp.flight.converter;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.tokioschool.flightapp.core.converter.ModelMapperConfiguration;
import org.tokioschool.flightapp.flight.domain.Airport;
import org.tokioschool.flightapp.flight.domain.Flight;
import org.tokioschool.flightapp.flight.domain.FlightImage;
import org.tokioschool.flightapp.flight.domain.FlightStatus;
import org.tokioschool.flightapp.flight.dto.FlightDTO;
import org.tokioschool.flightapp.flight.dto.ResourceDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public class FlightConverterTest {
    //creamos un objeto de tipo ModelMapper con la configuracion de ModelMapperConfiguration que creamos
    private final ModelMapper modelMapper = new ModelMapperConfiguration().modelMapper();

    @Test
    void givenUser_whenConverterToDTOP_thenOK(){
        //creamos un objeto de tipo Flight
        Flight flight = Flight.builder()
                .id(1L)
                .number("IB2020")
                .departure(Airport.builder().acronym("MAD").build())
                .arrival(Airport.builder().acronym("BCN").build())
                .departureTime(LocalDateTime.now())
                .status(FlightStatus.SCHEDULED)
                .capacity(100)
                .occupancy(50)
                .image(FlightImage.builder()
                        .contentType("content-type")
                        .size(10)
                        .filename("filename")
                        .resourceId(UUID.randomUUID())
                        .build())
                .build();

        //convertimos el objeto Flight a FlightDTO con el modelMapper
        FlightDTO flightDTO = modelMapper.map(flight,FlightDTO.class);

        //comprobamos que los valores de los atributos de Flight y FlightDTO son iguales
        Assertions.assertThat(flightDTO)
                .returns(flight.getId(), FlightDTO::getId)
                .returns(flight.getCapacity(), FlightDTO::getCapacity)
                .returns(flight.getNumber(), FlightDTO::getNumber)
                .returns(flight.getDeparture().getAcronym(), FlightDTO::getDepartureAcronym)
                .returns(flight.getArrival().getAcronym(), FlightDTO::getArrivalAcronym)
                .returns(flight.getDepartureTime(), FlightDTO::getDepartureTime)
                .returns(flight.getStatus().name(), o -> o.getStatus().name())
                .satisfies(o -> Assertions.assertThat(o.getImage()).isNotNull());

        ResourceDTO resourceDTO = flightDTO.getImage();

        Assertions.assertThat(resourceDTO)
                .returns("content-type", ResourceDTO::getContentType)
                .returns(10, ResourceDTO::getSize)
                .returns("filename", ResourceDTO::getFilename)
                .satisfies( o -> Assertions.assertThat(o.getResourceId()).isNotNull());
    }
}
