package org.tokioschool.flightapp.helloworld.mvc;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.tokioschool.flightapp.helloworld.dto.HiMessageResponseDTO;
import org.tokioschool.flightapp.helloworld.service.HelloWorldService;

@WebMvcTest(HelloWorldMvcController.class)
@AutoConfigureMockMvc(addFilters = false)//Se desactivan los filtros de seguridad
public class HelloWorldMvcControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HelloWorldService helloWorldService;

    @Test
    void givenGetHiRequest_whenRequested_thenResponseOk() throws Exception {
        String hiMessage = "Hello World test";

        Mockito.when(helloWorldService.getHiMessage("bla"))
                .thenReturn(new HiMessageResponseDTO(hiMessage));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/helloworld?name=bla"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("helloworld/index"))
                .andExpect(MockMvcResultMatchers.model().attribute("message", hiMessage));

    }

}
