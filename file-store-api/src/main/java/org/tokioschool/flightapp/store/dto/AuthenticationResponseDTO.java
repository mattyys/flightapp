package org.tokioschool.flightapp.store.dto;


import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class AuthenticationResponseDTO {

    String accessToken;
    long expiresIn;
}
