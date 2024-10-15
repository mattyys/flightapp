package org.tokioschool.flightapp.store.service;

import org.tokioschool.flightapp.store.dto.AuthenticatedMeResponseDTO;
import org.tokioschool.flightapp.store.dto.AuthenticationRequestDTO;
import org.tokioschool.flightapp.store.dto.AuthenticationResponseDTO;

public interface AuthenticationService {

    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO);

    AuthenticatedMeResponseDTO getAuthenticated();

}
