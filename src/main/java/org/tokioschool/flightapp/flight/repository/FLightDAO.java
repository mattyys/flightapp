package org.tokioschool.flightapp.flight.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tokioschool.flightapp.flight.domain.Flight;
import org.tokioschool.flightapp.flight.domain.FlightStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FLightDAO extends JpaRepository<Flight, Long> {


    List<Flight> findByDepartureAcronym (String departureAcronym);

    List<Flight> findByDepartureTimeIsAfterAndStatusIs(LocalDateTime departureTimeFrom, FlightStatus status);

    //metodos que buescan por el numero de vuelo podemos usar like para buscar por una parte del numero
    List<Flight> findByNumberLike(String number);

    //metodo que busca por el numero de vuelo que contenga una parte de la cadena que le pasamos
    List<Flight> findByNumberContains(String number);

    //devolver resultados paginados
    //por defecto jpa devulve resultados paginado por findAll
}
