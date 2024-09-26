package org.tokioschool.flightapp.flight.service;

import org.apache.commons.lang3.tuple.Pair;
import org.tokioschool.flightapp.flight.dto.UserDTO;

import java.util.Optional;

public interface UserService {

    //Pair de common lang devuelve una tupla con dos valores
    Optional<Pair<UserDTO, String>> findUserAndPasswordByEmail(String email);

    Optional<UserDTO> findByEmail(String email);

}
