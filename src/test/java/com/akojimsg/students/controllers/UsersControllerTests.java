package com.akojimsg.students.controllers;

import com.akojimsg.students.AbstractApplicationContextTest;
import com.akojimsg.students.data.entities.User;
import com.akojimsg.students.data.repositories.StudentRepository;
import com.akojimsg.students.data.repositories.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@AutoConfigureMockMvc(addFilters = false)
public class UsersControllerTests extends AbstractApplicationContextTest {
  @TestConfiguration
  static class UserControllerTestsConfiguration {
    @Bean
    CommandLineRunner initUserControllerTests(UserRepository repository) {
      return args -> {
        try (InputStream inputStream = TypeReference.class.getResourceAsStream("/static/users.json")) {
          ObjectMapper mapper = new ObjectMapper();
          mapper.registerModule(new JavaTimeModule());
          mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
          List<User> usersList = mapper.readValue(inputStream, new TypeReference<List<User>>() {});
          repository.saveAll(usersList);
        }
      };
    }
  }

  @Autowired
  MockMvc mockMvc;
  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("Test GET /v1/Users and receive 200 OK response")
  void getUsers_whenValidRequestIsReceived_returns200Response() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.get("/v1/users")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON);

    MvcResult response = mockMvc.perform(request).andReturn();
    String responseBody = response.getResponse().getContentAsString();
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    List<User> users = mapper.readValue(responseBody, new TypeReference<List<User>>() {});
    Assertions.assertEquals(response.getResponse().getStatus(),200);
    Assertions.assertTrue(users.size() >= 20);
  }

  @Test
  @DisplayName("Test GET /v1/users/{id} with 200 OK response")
  void getUserById_whenIdIsValid_returns200Response() throws Exception {

    RequestBuilder request = MockMvcRequestBuilders.get("/v1/users/1")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON);

    MvcResult response = mockMvc.perform(request).andReturn();
    String responseBody = response.getResponse().getContentAsString();

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    User users = mapper.readValue(responseBody, User.class);
    Assertions.assertEquals(response.getResponse().getStatus(), 200);
  }

  @Test
  @DisplayName("Test GET /v1/users/{id} with 400 Not Found Exception")
  void getUserById_whenIdIsValid_returns400Response() throws Exception {

    RequestBuilder request = MockMvcRequestBuilders.get("/v1/users/100")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON);

    MvcResult response = mockMvc.perform(request).andReturn();
    String responseBody = response.getResponse().getContentAsString();

    ObjectMapper mapper = new ObjectMapper();
    Assertions.assertEquals(response.getResponse().getStatus(), 404);
  }
}
