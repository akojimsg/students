package com.akojimsg.students.controllers;

import com.akojimsg.students.AbstractApplicationContextTest;
import com.akojimsg.students.data.dto.SignUpDTO;
import com.akojimsg.students.data.entities.UserAccount;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class AuthControllerTests extends AbstractApplicationContextTest {

  @TestConfiguration
  static class AuthControllerTestsConfiguration {
  }

  @Autowired
  MockMvc mockMvc;

  @Test
  @DisplayName("test POST v1/auth/signup")
  void testSignUp_whenValidUserDetailsProvided_returnsCreateUserDetails() throws Exception {
    SignUpDTO dto = new SignUpDTO("lorem", "ipsium", List.of("ROLE_TEST"));
    RequestBuilder request = MockMvcRequestBuilders.post("/v1/auth/signup")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(dto));

    MvcResult response = mockMvc.perform(request).andReturn();
    String responseBody = response.getResponse().getContentAsString();
    ObjectMapper mapper = new ObjectMapper();
    JsonNode userAccount = mapper.readTree(responseBody);
    //UserAccount userAccount = mapper.readValue(responseBody, UserAccount.class);

    Assertions.assertEquals(dto.getUsername(), userAccount.get("username").asText());
    //Assertions.assertEquals(dto.getUsername(), userAccount.getUsername());
  }
}
