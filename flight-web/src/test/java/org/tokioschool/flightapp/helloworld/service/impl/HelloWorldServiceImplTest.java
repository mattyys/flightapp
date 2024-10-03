package org.tokioschool.flightapp.helloworld.service.impl;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.tokioschool.flightapp.helloworld.dto.HiMessageResponseDTO;
import org.tokioschool.flightapp.helloworld.service.HelloWorldService;

class HelloWorldServiceImplTest {

    private final HelloWorldService helloWorldService = new HelloWorldServiceImpl();

    @Test
    void givenNUllName_whenGetHiMessage_thenReturnOk(){
        HiMessageResponseDTO response = helloWorldService.getHiMessage(null);

        Assertions.assertEquals("Hello World dude", response.getMessage());
    }

    @Test
    void givenEmptyName_WhenGetHiMessage_thenReturnOk(){
        HiMessageResponseDTO response = helloWorldService.getHiMessage("");

        Assertions.assertEquals("Hello World dude", response.getMessage());
    }

    @Test
    void givenEmptyNoTrimmedName_WhenGetHiMessage_thenReturnOk(){
        HiMessageResponseDTO response = helloWorldService.getHiMessage("   ");

        Assertions.assertEquals("Hello World dude", response.getMessage());
    }

    @Test
    void givenName_WhenGetHiMessage_thenReturnOk() {
        HiMessageResponseDTO response = helloWorldService.getHiMessage("pedrito");

        Assertions.assertEquals("Hello World pedrito", response.getMessage());
    }

}