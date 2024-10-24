package org.tokioschool.flightapp.domain;


import jakarta.persistence.Embeddable;
import lombok.*;


@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirportRawId {
    private Long jobId;
    private String acronym;
}
