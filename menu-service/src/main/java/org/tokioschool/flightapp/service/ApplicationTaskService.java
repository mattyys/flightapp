package org.tokioschool.flightapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.tokioschool.flightapp.MenuServiceApplication;
import org.tokioschool.flightapp.domain.Main;
import org.tokioschool.flightapp.domain.Menu;
import org.tokioschool.flightapp.repository.MenuDAO;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationTaskService implements ApplicationRunner {

  private final MenuService menuService;
  private final MenuDAO menuDAO;
  private final MenuServiceApplication menuServiceApplication;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info("ApplicationTakService started");

    long countMenus = menuDAO.count();
    log.info("Count menus: {}", countMenus);

    if( countMenus == 0){
      Collection<Menu> randomMenus = menuService.createRandomMenus();
      String randomId = randomMenus.toArray(new Menu[0])[10].getId();

      log.info("Random id: {}", randomId);
      Optional<Menu> menu = Optional.ofNullable(menuDAO.findById(randomId).orElseGet(() -> Menu.builder().build()));

      log.info("Menu random: {}", menu);
    }

    //Recuperar los ids de menus
    List<String> menuIds = menuService.getMenuIds();
    log.info("Menu ids: {}", String.join(", ", menuIds));

    //Filtrar los menus vegetarianos y ordenarlos por titulos
    List<Menu> vegetarianMenus = menuDAO.findByVegetarianIsTrueOrderByTitle();
    log.info(
        "Vegetarian menus: {}, first:{}, last:{}",
        vegetarianMenus.size(),
        vegetarianMenus.get(0).getTitle(),
        vegetarianMenus.get(vegetarianMenus.size()-1).getTitle());

    //Ordenar por campos nested en los documentos:
    //no ordenado
    Menu menu11 = menuDAO.findById(menuIds.get(11)).get();
    log.info(
            "Menu 11 non-ordered: {}-{}",
            menu11.getTitle(),
            menu11.getMains().stream().map(Main::getName).toList());

    //ordenado
    Menu menu11Ordered = menuDAO.findByIdWithMainsOrdered(menuIds.get(11)).get();
    log.info(
            "Menu 11 yes-ordered: {}-{}",
            menu11Ordered.getTitle(),
            menu11Ordered.getMains().stream().map(Main::getName).toList());

    //Modificar docuementos, via insert
    Menu vegetarianMenu = vegetarianMenus.get(0);
    vegetarianMenu.setVegetarian(false);
    //menuDAO.insert(vegetarianMenu); por insert no se puede realizar la modificacion

    //via save
    menuDAO.save(vegetarianMenu);
    //para verificar hacemos un count
    long countByVegetarianIsTrue = menuDAO.countByVegetarianIsTrue();
    log.info("Vegetarian menus: {}", countByVegetarianIsTrue);

    //Filtrar por texto, en un campo nested
    List<Menu> pizzaMenusByMAinName = menuDAO.findByMainsName("pizza");
    log.info("Pizza menus case-sensitive:{}", pizzaMenusByMAinName.size());

    List<Menu> pizzaMenusByMAinName2 = menuDAO.findByMainsName("Pizza");
    log.info("Pizza menus case-sensitive:{}", pizzaMenusByMAinName2.size());

    List<Menu> pizzaMenusCI = menuDAO.findByMainsNameCaseInsensitive("pizza");
    log.info("Pizza menus case-insensitive:{}", pizzaMenusCI.size());

    //Filtrar por valores numericos
    List<Menu> byCaloriesGreaterThan = menuDAO.findByCaloriesGreaterThan(BigDecimal.valueOf(650));
    log.info("Calories greater than 650: {}", byCaloriesGreaterThan.size());

    //Calcular la media de calorias
    Double averageCalories = menuDAO.findCaloriesAverage();
    log.info("Average calories: {}", averageCalories);

    //Filtrar por un criterio nested y devolver (projection) los campos que hacen matching
    List<Menu> menuWithMainsWithLettuce = menuDAO.findMainsByIngredient("lettuce");
    log.info("Menus with lettuce: {}", menuWithMainsWithLettuce.size());

    Menu menuAndMainsWithLettuce = menuWithMainsWithLettuce.get(0);
    Menu menuAndMainsWithLettuceAndOthers = menuDAO.findById(menuAndMainsWithLettuce.getId()).get();
    log.info("Menu mains complete: {}", menuAndMainsWithLettuceAndOthers.getMains());
    log.info("Menu mains lettuce: {}", menuAndMainsWithLettuce.getMains());

  }
}
