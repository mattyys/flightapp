package org.tokioschool.flightapp.repository;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.tokioschool.flightapp.domain.Beer;
import org.tokioschool.flightapp.projection.BeerStyleCountAggregate;

import java.util.List;
import java.util.UUID;

public interface BeerDAO extends MongoRepository<Beer, UUID> {

  @Aggregation(
      pipeline = {
        "{$group:  {'_id':'$style', 'count': {'$sum':1}}}",
        "{$project: {'_id': null, 'style': '$_id', 'count':  '$count'}}",
        "{$sort:  {'count':  -1}}"
      })
  List<BeerStyleCountAggregate> countByStyle();

  List<Beer> findByStyleIsNot(String style);

  List<Beer> findByStyle(String style);
}
