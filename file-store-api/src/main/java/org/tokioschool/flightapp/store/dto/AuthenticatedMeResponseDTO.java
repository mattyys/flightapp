package org.tokioschool.flightapp.store.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class AuthenticatedMeResponseDTO {

    String username;
    List<String> authorities;
}
