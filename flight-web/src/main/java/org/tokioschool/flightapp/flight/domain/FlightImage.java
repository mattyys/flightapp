package org.tokioschool.flightapp.flight.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "flight_images")
public class FlightImage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  @JdbcTypeCode(
      SqlTypes
          .VARCHAR) // esta anotacion se define para que el tipo de dato sea varchar en la base de
  // datos porque en mysql o mariadb es complejo de manejar el tipo de dato UUID
  private UUID resourceId;

  private String filename;
  private String contentType;
  private int size;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "flight_id")
  private Flight flight;
}
