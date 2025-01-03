package org.tokioschool.flightapp.store.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.tokioschool.flightapp.store.dto.ResourceContentDTO;
import org.tokioschool.flightapp.store.security.StoreApiSecurityConfiguration;
import org.tokioschool.flightapp.store.service.StoreService;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@WebMvcTest(ResourceApiController.class)
@ContextConfiguration(
    classes = {
      ResourceApiController.class,
      ResourceApiControllerTest.Init.class,
      StoreApiSecurityConfiguration.class
    })
class ResourceApiControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private StoreService storeService;
  @Autowired private ObjectMapper objectMapper;

  @Test
  @WithMockUser(username = "username", authorities = "read-resource2")
  void givenExistingResource_whenGet_thenRetrievedOk() throws Exception {

    // prepare - preparo todos los objetos necesarios para la prueba
    // execution - ejecuto la prueba
    // assert - compruebo que el resultado es el esperado

    // prepare
    ResourceContentDTO resourceContentDTO =
        ResourceContentDTO.builder()
            .resourceId(UUID.randomUUID())
            .description("description")
            .contentType("text/plain")
            .filename("hello.txt")
            .size("hello".length())
            .content("Hello".getBytes(StandardCharsets.UTF_8))
            .build();

    Mockito.when(storeService.findResource(resourceContentDTO.getResourceId()))
        .thenReturn(Optional.of(resourceContentDTO));

    // execution
    MvcResult mvcResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get(
                    "/store/api/resources/{resourceId}", resourceContentDTO.getResourceId()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    // assert
    ResourceContentDTO response =
        objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), ResourceContentDTO.class);

    // conversion de base64 a bytes
    Map<String, Object> values =
        objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});

    // conversion de bytes a base64
    String base64 = Base64.getEncoder().encodeToString(resourceContentDTO.getContent());

    Assertions.assertThat(values).containsEntry("content", base64);

    Assertions.assertThat(response.getContent()).isEqualTo(resourceContentDTO.getContent());
  }

  @TestConfiguration
  public static class Init {
    @Bean
    public JwtDecoder jwtDecoder() {
      return NimbusJwtDecoder.withSecretKey(new SecretKeySpec("SECRET".getBytes(), "HMAC")).build();
    }
  }
}
