package org.tokioschool.flightapp.repository;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.tokioschool.flightapp.domain.Menu;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuDAO extends MongoRepository<Menu, String> {
  List<Menu> findByVegetarianIsTrueOrderByTitle();

  @Aggregation(
      pipeline = {
        "{$match: {id: '?0'}}",
        "{$set:  {mains: {$sortArray:  {input:  '$mains', sortBy:  {name:  -1}}}}}"
      })
  Optional<Menu> findByIdWithMainsOrdered(String s);

  long countByVegetarianIsTrue();

  List<Menu> findByMainsName(String pizza);

  @Query(
      """
              {
              mains: {
              $elemMatch: {
              name: {$regex: '?0', $options: 'i'}}}
              }
              """)
  List<Menu> findByMainsNameCaseInsensitive(String pizza);

  List<Menu> findByCaloriesGreaterThan(BigDecimal bigDecimal);

  @Aggregation(pipeline = {"{$group: {_id: null, total: {$avg: '$calories'}}}"})
  Double findCaloriesAverage();

  @Query(
      value =
          """
              {
                 'mains.ingredients': {
                    $elemMatch: {
                      name: {
                              $regex: '?0',
                               $options: 'i'
                          }
                       }
                 }
              }
          """,
      fields = "{'mains.$':  1}")
  List<Menu> findMainsByIngredient(String lettuce);


//  List<Menu> finByBeerStyle(String lightLager);

  List<Menu> findByBeerIn(Collection<UUID> beerIds);

}
