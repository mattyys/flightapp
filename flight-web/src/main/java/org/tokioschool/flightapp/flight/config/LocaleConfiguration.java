package org.tokioschool.flightapp.flight.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

@Configuration
public class LocaleConfiguration implements WebMvcConfigurer {

  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
    LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
    localeChangeInterceptor.setParamName(
        "lang"); // parametro que se usará para cambiar el idioma cuando lo detecta lo utiliza
    return localeChangeInterceptor;
  }

  // una vez interceptado el cambio de idioma, se debe registrar el cambio por medio de
  // LocaleResolver y lo hacemos a traves de cookies
  @Bean
  public LocaleResolver localeResolver() {
    CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
    cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);//en caso de no existir se le indica por defecto el ingles
    return cookieLocaleResolver;
  }

  //se indica que el Interceptor este en la cadena MVC


  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor());
  }
}
//quede en 1hminuto1 no carga por defecto los mensajes en ingles