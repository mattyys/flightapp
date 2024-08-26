package org.tokioschool.flightapp.helloworld.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.tokioschool.flightapp.helloworld.dto.HiMessageResponseDTO;
import org.tokioschool.flightapp.helloworld.service.HelloWorldService;

@WebMvcTest(HelloWorldApiController.class)
class HelloWorldApiControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockBean private HelloWorldService helloWorldService;

    @Test
    void givenGetHiRequest_whenRequested_thenResponseOk() throws Exception {
        String hiMessage = "Hello World test";

        Mockito.when(helloWorldService.getHiMessage("bla"))
                .thenReturn(new HiMessageResponseDTO(hiMessage));

        //con mockMvc no es necesario levantar el servidor, solo se simula la petición
        String response = mockMvc
                .perform(MockMvcRequestBuilders.get("/helloworld/api/hi?name=bla"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //Se convierte el JSON a un objeto de tipo HiMessageResponseDTO por medio de reflexión del método readValue
        HiMessageResponseDTO hiMessageResponseDTO = objectMapper.readValue(response, HiMessageResponseDTO.class);

        Assertions.assertEquals(hiMessage, hiMessageResponseDTO.getMessage());
    }

}