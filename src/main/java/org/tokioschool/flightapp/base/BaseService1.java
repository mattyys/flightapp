package org.tokioschool.flightapp.base;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@DependsOn({"baseService3"})
public class BaseService1 {

    //3ra forma de inyectar dependencias a travez de un constructor es la fomra mas recomendada

    private final BaseService2 baseService2;


    @PostConstruct
    protected void postConstruct(){
        log.info("baseService1, {}", baseService2.say());
    }

    private String say(){
        return "Hello from Service1";
    }
}
