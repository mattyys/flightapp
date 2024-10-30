package org.tokioschool.flightapp.service;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.tokioschool.flightapp.domain.Ingredient;
import org.tokioschool.flightapp.domain.Main;
import org.tokioschool.flightapp.domain.Menu;
import org.tokioschool.flightapp.repository.MenuDAO;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuService {

  private final MenuDAO menuDAO;
  private final MongoTemplate mongoTemplate;
  private final SecureRandom secureRandom = new SecureRandom();

  public Collection<Menu> createRandomMenus() {

    Faker faker = new Faker();

    List<Menu> menusToCreate =
        IntStream.range(0, 100)
            .mapToObj(
                i -> {
                  List<Main> mains =
                      IntStream.range(0, i)
                          .mapToObj(
                              j ->
                                  Main.builder()
                                      .name(faker.food().dish())
                                      .ingredients(
                                          List.of(
                                              Ingredient.builder()
                                                  .name(faker.food().ingredient())
                                                  .build(),
                                              Ingredient.builder()
                                                  .name(faker.food().vegetable())
                                                  .build()))
                                      .build())
                          .toList();

                  return Menu.builder()
                      .title("menu-%d".formatted(i))
                      .created(Instant.now())
                      .calories(BigDecimal.valueOf(secureRandom.nextDouble(300, 1000)))
                      .vegetarian(secureRandom.nextBoolean())
                      .mains(mains)
                      .build();
                })
            .toList();

    return mongoTemplate.insertAll(menusToCreate);
  }

    public List<String> getMenuIds() {

        Query query = new Query();
        query.fields().include("_id");
        
        return mongoTemplate.find(query, Menu.class).stream().map(Menu::getId).toList();
    }
}
