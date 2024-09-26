package org.tokioschool.flightapp.flight.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.tokioschool.flightapp.flight.dto.UserDTO;
import org.tokioschool.flightapp.flight.repository.UserDAO;
import org.tokioschool.flightapp.flight.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final ModelMapper modelMapper;


    @Override
    public Optional<Pair<UserDTO, String>> findUserAndPasswordByEmail(String email) {

        return userDAO.findByEmail(email)
                .map(user -> Pair.of(modelMapper.map(user, UserDTO.class), user.getPassword()));
    }

    @Override
    public Optional<UserDTO> findByEmail(String email) {
        return userDAO.findByEmail(email)
                .map(user -> modelMapper.map(user, UserDTO.class));
    }
}
