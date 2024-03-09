package org.icipe.lima.auth.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.icipe.lima.auth.request.CreateClientRequest;
import org.icipe.lima.auth.service.RegisteredClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class RegisteredDashboardControllerTest {
  @Autowired MockMvc mockMvc;
  @Autowired ObjectMapper objectMapper;
  @MockBean RegisteredClientService registeredClientService;

  @WithMockUser()
  @Test
  void givenValidClientCredentials_thenCreateNewClient() throws Exception {
    CreateClientRequest clientRequest =
        CreateClientRequest.builder()
            .clientName("Sample Client")
            .clientId("test-client")
            .clientSecret("Secret@123")
            .redirectUris("https://example.com/auth")
            .logoutRedirectUri("http://example.com/home")
            .grantTypes(
                new String[] {
                  AuthorizationGrantType.JWT_BEARER.getValue(),
                  AuthorizationGrantType.REFRESH_TOKEN.getValue()
                })
            .authenticationMethods(
                new String[] {ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue()})
            .build();

    mockMvc
        .perform(
            post("/clients")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(objectMapper.writeValueAsString(clientRequest)))
        .andExpect(status().isCreated());
  }
}
