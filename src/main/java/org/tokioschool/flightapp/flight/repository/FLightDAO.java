package org.tokioschool.flightapp.flight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tokioschool.flightapp.flight.domain.Flight;
import org.tokioschool.flightapp.flight.domain.FlightStatus;
import org.tokioschool.flightapp.flight.projection.FlightCancelledCountryByAirport;
import org.tokioschool.flightapp.flight.projection.FlightCountryByAirport;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FLightDAO extends JpaRepository<Flight, Long> {

  List<Flight> findByDepartureAcronym(String departureAcronym);

  List<Flight> findByDepartureTimeIsAfterAndStatusIs(
      LocalDateTime departureTimeFrom, FlightStatus status);

  // metodos que buescan por el numero de vuelo podemos usar like para buscar por una parte del
  // numero
  List<Flight> findByNumberLike(String number);

  // metodo que busca por el numero de vuelo que contenga una parte de la cadena que le pasamos
  List<Flight> findByNumberContains(String number);

  // devolver resultados paginados
  // por defecto jpa devulve resultados paginado por findAll

  // projection consulta jpql que nos devuelve un objeto con los datos que necesitamos
  @Query(
      """
            SELECT f.departure.acronym AS acronym, COUNT(1) AS counter
            FROM Flight f
            GROUP BY f.departure.acronym
            """)
  List<FlightCountryByAirport> getFlightCountryByDepartureAirport();

  //Esta consulta es en sql y relaiza lo mismo que la anterior
  @Query(value = """
          SELECT airports.id AS acronym, COUNT(1) AS counter
          FROM flights
          JOIN  airports ON flights.airport_arrival_id = airports.id
          GROUP BY  airports.id
          """, nativeQuery = true)
  List<FlightCountryByAirport> getFlightCountryByArrivalAirport();

  // consulta jpql que nos devuelve un objeto con los datos que necesitamos
  //se pasa el parametro status que es el estado del vuelo
  @Query(
      """
                  select new org.tokioschool.flightapp.flight.projection.FlightCancelledCountryByAirport(f.departure.acronym, count(1))
                  from  Flight f
                  where f.status = :status
                  group by f.departure.acronym
              """)
  List<FlightCancelledCountryByAirport> getFlightStatusCountryByAirport(
      @Param("status") FlightStatus status);

  // update de un numero de vuelo
  @Query("""
          UPDATE Flight  f
          Set f.number = :number
          WHERE f.id =:id
          """)
  @Modifying(clearAutomatically = true) // le indicamos que es una consulta que modifica datos y que limpie la cache
  int updateFlightNumber(@Param("id") Long flightId, @Param("number") String number);//devuelve el numero de filas afectadas
}
