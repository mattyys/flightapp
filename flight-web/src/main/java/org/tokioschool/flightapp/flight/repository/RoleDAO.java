package org.tokioschool.flightapp.flight.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tokioschool.flightapp.flight.domain.Role;

@Repository
public interface RoleDAO extends CrudRepository<Role, Long> {}
