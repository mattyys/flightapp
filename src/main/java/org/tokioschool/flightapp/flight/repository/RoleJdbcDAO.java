package org.tokioschool.flightapp.flight.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.tokioschool.flightapp.flight.domain.Role;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleJdbcDAO {

  private final NamedParameterJdbcTemplate
      namedParameterJdbcTemplate; // sirve para ejecutar consultas sql y mapear los resultados
  private final JdbcTemplate jdbcTemplate; // sirve para ejecutar consultas sql

  private final RowMapper<Role> rowMapper =
      (rs, rowNum) -> Role.builder().id(rs.getLong("id")).name(rs.getString("name")).build();

  // metodo para contar los roles de la base de datos
  public int countRoles() {
    String sql = "SELECT COUNT(1) AS count FROM roles"; // consulta sql para contar los roles

    RowMapper<Integer> counterMapper =
        ((rs, rowNum) -> rs.getInt("count")); // mapper recupera el count del query

    return namedParameterJdbcTemplate.queryForObject(
        sql, Map.of(), counterMapper); // ejecutamos la consulta y devolvemos el resultado
  }

  // insertar un rol a la base de datos
  public Long insertRole(Role role) {

    SimpleJdbcInsert simpleJdbcInsert =
        new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("roles")
            .usingGeneratedKeyColumns(
                "id"); // indicamos el nombre de la tabla y que la columna id es autoincremental

    Map<String, String> params =
        Map.of("name", role.getName()); // indicamos los parametros a insertar
    // simpleJdbcInsert recibe un map con los parametros a insertar osea clave valor
    return simpleJdbcInsert
        .executeAndReturnKey(params)
        .longValue(); // ejecutamos la insercion y devolvemos el id generado
  }

  public Optional<Role> findRoleById(Long id) {
    String sql =
        """
        SELECT id, name
        FROM roles
        WHERE id = :id
      """; // consulta sql para buscar un rol por id

    Role role = namedParameterJdbcTemplate.queryForObject(sql, Map.of("id", id), rowMapper);

    return Optional.ofNullable(role); // ejecutamos la consulta y devolvemos el resultado
  }

  public int deleteRoleByName(String name){

    String sql = "DELETE FROM roles WHERE name = :name";

    return namedParameterJdbcTemplate.update(sql,Map.of("name",name));
  }


}
