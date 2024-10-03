package org.tokioschool.flightapp.flight.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.tokioschool.flightapp.flight.domain.Role;
import org.tokioschool.flightapp.flight.domain.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(
    properties = {
      "spring.datasource.url+jdbc:h2:mem:testdb;MODE=MYSQL;DATABASE_TO_LOWER=TRUE",
      "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
      "spring.jpa.hibernate.ddl-auto=create-drop",
    })
@Transactional(
    propagation =
        Propagation.NOT_SUPPORTED) // con esto evitamos que se hagan rollback de las transacciones
class UserDAOTest {

  @Autowired private RoleDAO roleDAO;
  @Autowired private UserDAO userDAO;

  @Autowired
  private TransactionTemplate transactionTemplate; // para crear el contexto de transacción

  @BeforeEach
  void beforeEach() {

    Role role1 = Role.builder().name("ROLE_ADMIN").build();
    Role role2 = Role.builder().name("ROLE_USER").build();

    roleDAO.saveAll(List.of(role1, role2));

    User user1 =
        User.builder()
            .name("user")
            .surname("usersurname")
            .password("pass")
            .roles(new HashSet<>())
            .email("user@email.com")
            .build();

    User user2 =
        User.builder()
            .name("admin")
            .surname("adminsurname")
            .password("pass")
            .roles(new HashSet<>(Set.of(role1, role2)))
            .email("admin@email.com")
            .build();

    userDAO.saveAll(List.of(user1, user2));
  }

  @AfterEach
  void afterEach() {
    userDAO.deleteAll();
    roleDAO.deleteAll();
  }

  // Verificar si los usuarios se agregaron correctamente
  @Test
  void givenTwoUsers_whenFindAll_thenReturnAll() {

    transactionTemplate.executeWithoutResult(// ejecutamos sin transacción
        transactionStatus -> {
          List<User> users = userDAO.findAll();

          Assertions.assertThat(users).hasSize(2);

          Assertions.assertThat(
                  users.stream()
                      .filter(user -> user.getEmail().equals("admin@email.com"))
                      .findFirst()
                      .get())
              .returns("admin", User::getName)
              .satisfies(user -> Assertions.assertThat(user.getCreated()).isNotNull())
              .satisfies(user -> Assertions.assertThat(user.getId()).isNotNull())
              .satisfies(
                  user ->
                      Assertions.assertThat(user.getRoles().stream().map(Role::getName).toList())
                          .containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN"));
        });
  }

  @Test
  void givenUser_whenDeleted_thenManyToManyIsDeleted() {
    User user = userDAO.findByEmail("admin@email.com").get();

    userDAO.delete(user);

    List<User> users = userDAO.findAll();

    Assertions.assertThat(users).hasSize(1);
  }
}
