package org.tokioschool.flightapp.flight.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.UUID;

@Configuration
@Slf4j
public class LoggerConfiguration implements WebMvcConfigurer {

  @Bean
  public HandlerInterceptor loggerHandlerInterceptor() {
    return new HandlerInterceptor() {

      // bandera para filtrar las peticiones que sean de flight
      private boolean filter(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/flight");
      }

      @Override
      public boolean preHandle(
          HttpServletRequest request, HttpServletResponse response, Object handler)
          throws Exception {
        // si la petición es de flight se añade el atributo ts con el tiempo actual
        if (filter(request)){
            request.setAttribute("ts", System.currentTimeMillis());
            //añade un identificador único a la petición la cual se puede utilizar para seguimiento
            //debemos agregarla al archivo logback-spring.xml
            MDC.put("request-ID", UUID.randomUUID().toString());
            log.info("Request {} {}", request.getMethod(), request.getRequestURI());
        }

        return true;
      }

      @Override
      public void postHandle(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler,
          ModelAndView modelAndView)
          throws Exception {

        // si la petición es de flight se muestra el tiempo que ha tardado en procesarse
        if (filter(request)) {
          long ts = (long) request.getAttribute("ts");
          log.debug(
              "response:{} {} ms:{}",
              request.getMethod(),
              request.getRequestURI(),
              System.currentTimeMillis() - ts);
        }
      }

      @Override
      public void afterCompletion(
          HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
          throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
      }
    };
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(loggerHandlerInterceptor());
  }
}
