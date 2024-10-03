package org.tokioschool.flightapp.flight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tokioschool.flightapp.flight.domain.User;

import java.util.Optional;


@Repository
public interface UserDAO extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

}
