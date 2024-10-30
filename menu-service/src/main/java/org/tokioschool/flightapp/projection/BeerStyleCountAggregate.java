package org.tokioschool.flightapp.projection;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BeerStyleCountAggregate {
    private String style;
    private Long count;
}
