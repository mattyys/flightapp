package org.tokioschool.flightapp.flight.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDTO {

    private String url;
    private String exception;
}
