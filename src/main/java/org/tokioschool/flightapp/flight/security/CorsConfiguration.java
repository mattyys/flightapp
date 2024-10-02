package org.tokioschool.flightapp.flight.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/flight/**")
                .allowedMethods(HttpMethod.POST.name(), HttpMethod.GET.name())
                .allowedOrigins("http://localhost:8080");//el origen de la petición debe ser el mismo que el del cliente

        //agregamos el login que esta fuera del dominioo de /flight/**
        registry.addMapping("/login")
                .allowedMethods(HttpMethod.POST.name(), HttpMethod.GET.name())
                .allowedOrigins("http://localhost:8080");

    }
}
