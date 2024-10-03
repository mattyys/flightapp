package org.tokioschool.flightapp.flight.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.tokioschool.flightapp.flight.domain.Role;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;

@DataJpaTest(
    properties = {
      "spring.datasource.url+jdbc:h2:mem:testdb;MODE=MYSQL;DATABASE_TO_LOWER=TRUE",
      "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
      "spring.jpa.hibernate.ddl-auto=create-drop",
    })
class RoleJdbcDAOTest {

  private RoleJdbcDAO roleJdbcDAO;

  @Autowired private RoleDAO roleDAO;
  @Autowired private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  @Autowired private JdbcTemplate jdbcTemplate;

  // RoleJdbcDAO es una clase comun entonces debenmos crear la instancia en el metodo beforeEach
  @BeforeEach
  void beforeEach() {
    roleJdbcDAO = new RoleJdbcDAO(namedParameterJdbcTemplate, jdbcTemplate);

    Role role1 = Role.builder().name("role1").build();
    Role role2 = Role.builder().name("role2").build();

    roleDAO.saveAll(List.of(role1, role2));
  }

  @Test
  void givenRoles_whenCounter_thenReturnTwo() {
    int counter = roleJdbcDAO.countRoles();

    assertThat(counter).isEqualTo(2);
  }

  @Test
  void insertRole_whenCreated_thenReturnOk() {

    Role role = Role.builder().name("role3").build();
    Long roleId = roleJdbcDAO.insertRole(role);

    assertThat(roleId).isNotNull();

    Optional<Role> maybeRole = roleJdbcDAO.findRoleById(roleId);

    assertThat(maybeRole).isPresent();
    assertThat(maybeRole.get()).returns(roleId, Role::getId).returns("role3", Role::getName);
  }

  @Test
  void deleteRole_whenDeleteWithSqlInjection_thenReturnOk() {

    // int deleted = roleJdbcDAO.deleteRoleByName("role1");
    int deleted = roleJdbcDAO.deleteRoleByName("role1' OR '1' = '1");

    //assertThat(deleted).isOne();
    assertThat(deleted).isZero();
  }
}
