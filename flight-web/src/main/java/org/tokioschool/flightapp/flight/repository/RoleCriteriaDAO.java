package org.tokioschool.flightapp.flight.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.tokioschool.flightapp.flight.domain.Role;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleCriteriaDAO {

  private final EntityManager entityManager;//gestiona ciclos de vida de las entidades y permite operacines CRUD

  public Optional<Role> findRoleByName(String name) {

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();//crea instancias de CriteriaQuery, CriteriaUpdate, CriteriaDelete
    CriteriaQuery<Role> query = criteriaBuilder.createQuery(Role.class);//crea una consulta de tipo Role
    Root<Role> root = query.from(Role.class);//crea un nodo raiz para la consulta de tipo Role

    query.where(criteriaBuilder.equal(root.get("name"), name));//añade una condicion a la consulta where name = name

    try {
      Role singleResult = entityManager.createQuery(query).getSingleResult();//ejecuta la consulta y devuelve un solo resultado
      return Optional.of(singleResult);//devuelve el resultado
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }
}
