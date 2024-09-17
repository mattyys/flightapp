package org.tokioschool.flightapp.flight.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@RequiredArgsConstructor
public class ValidatorConfiguration {

  private final MessageSource messageSource;

  @Bean
  public LocalValidatorFactoryBean localValidatorFactoryBean() {
    LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();

    localValidatorFactoryBean.setValidationMessageSource(messageSource);
    localValidatorFactoryBean
        .afterPropertiesSet(); // aplica los cambios que se realizan en la configuracion y prepara
                               // el objeto para ser usado

    return localValidatorFactoryBean;
  }

  //quede en el minuto 38, ver documentos
}
