package org.tokioschool.flightapp.flight.repository;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.tokioschool.flightapp.flight.domain.Role;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest(
        properties = {
                "spring.datasource.url+jdbc:h2:mem:testdb;MODE=MYSQL;DATABASE_TO_LOWER=TRUE",
                "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
                "spring.jpa.hibernate.ddl-auto=create-drop",
        })
class RoleCriteriaDAOTest {

    @Autowired private EntityManager entityManager;

    private RoleCriteriaDAO roleCriteriaDAO;
    @Autowired
    private RoleDAO roleDAO;

    @BeforeEach
    void beforeEach() {
        roleCriteriaDAO = new RoleCriteriaDAO(entityManager);

        Role role1 = Role.builder().name("role1").build();

        roleDAO.save(role1);
    }

    @Test
    void givenRoles_whenFindByIdAndName_thenReturnOk(){
        Optional<Role> maybeROle = roleCriteriaDAO.findRoleByName("role1");

        Assertions.assertThat(maybeROle).isPresent();
    }

    @Test
    void givenRole_whenFindByNoExistingName_thenReturnOk(){

        Optional<Role> maybeRole = roleCriteriaDAO.findRoleByName("role2");

        Assertions.assertThat(maybeRole).isEmpty();

    }


}
