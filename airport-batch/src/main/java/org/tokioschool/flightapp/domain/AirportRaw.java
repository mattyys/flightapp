package org.tokioschool.flightapp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "airports_raw")
public class AirportRaw {

    @EmbeddedId
    private AirportRawId airportRawId;

    private String name;

    @Column(precision = 10, scale = 8)
    private BigDecimal lat;

    @Column(precision = 10, scale = 8)
    private BigDecimal lon;
    private String country;
}