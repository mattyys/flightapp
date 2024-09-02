package org.tokioschool.flightapp.flight.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceDTO {

    private UUID resourceId;
    private String filename;
    private String contentType;
    private int size;
}
