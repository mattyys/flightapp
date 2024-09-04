package org.tokioschool.flightapp.base.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BaseService3 {

    @PostConstruct
    protected void postConstruct(){
        log.info("baseService3, {}", "Hello from Service3");
    }

}
