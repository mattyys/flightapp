package org.tokioschool.flightapp.flight.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "airports")
public class Airport {

    @Id
    @Column(name = "id") //se especifica el nombre de la columna en la tabla
    private String acronym;

    private String name; //si no se especifica el nombre de la columna, se toma el nombre del atributo
    private String country;

    @Column(name="latitude")
    private BigDecimal lat;

    @Column(name="longitude")
    private BigDecimal lon;
}
