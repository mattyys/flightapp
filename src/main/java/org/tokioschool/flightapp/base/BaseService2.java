package org.tokioschool.flightapp.base;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Slf4j//se utiliza para poder hacer uso de los logs
@RequiredArgsConstructor
public class BaseService2 {

    @Lazy
    private final BaseService1 baseService1;

    public String say(){
        return "Hello from Service2";
    }
}
