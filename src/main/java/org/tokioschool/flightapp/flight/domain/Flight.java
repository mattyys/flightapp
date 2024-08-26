package org.tokioschool.flightapp.flight.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "flights")
public class Flight {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @CreationTimestamp private LocalDateTime created;

  private String number;

  @ManyToOne
  @JoinColumn(
      name = "airport_departure_id",
      nullable = false) // nullable = false significa que no puede ser nulo
  private Airport departure;

  @ManyToOne
  @JoinColumn(name = "airport_arrival_id", nullable = false)
  private Airport arrival;

  @Column(name = "departure_time")
  private LocalDateTime departureTime;

  @Enumerated(EnumType.STRING) // guarda el nombre del enum en la base de datos como string
  private FlightStatus status;

  @Column(nullable = false)
  private int capacity;

  @Column(nullable = false)
  private int occupancy;

  @OneToMany(
      mappedBy = "flight",
      cascade = CascadeType.ALL,
      orphanRemoval =
          true) // mappedby nos indica quien es el dueño de la relacion es quien guarda los
                // datos,cascade es la forma de borar y orphanRemoval = true le indicamos que se
                // borre si queda un dato huerfano
  private Set<FlightBooking> bookings;

  @OneToOne(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "flight_image_id", referencedColumnName = "id")
  private FlightImage image;
}
