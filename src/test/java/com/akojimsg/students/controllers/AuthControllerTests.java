package com.akojimsg.students.controllers;

import com.akojimsg.students.AbstractApplicationContextTest;
import com.akojimsg.students.data.dtos.SigninRequest;
import com.akojimsg.students.data.dtos.SignupRequest;
import com.akojimsg.students.data.entities.Role;
import com.akojimsg.students.data.entities.User;
import com.akojimsg.students.data.repositories.TokenRepository;
import com.akojimsg.students.services.UserService;
import com.akojimsg.students.utils.auth.JwtUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Optional;

@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTests extends AbstractApplicationContextTest {
  @FunctionalInterface
  interface MockAuthRequest<T, R> {
    MvcResult exec(R client, T request) throws Exception;
  }

  @TestConfiguration
  static class AuthControllerTestsConfiguration {
  }

  @Autowired
  MockMvc mockMvc;
  @Autowired
  JwtUtil jwtUtil;
  @Autowired
  UserService userService;
  @Autowired
  TokenRepository repository;
  static SigninRequest signinRequest;
  static SignupRequest signupRequest;
  static ObjectMapper mapper;

  @Value("${application.settings.default.userinfo-secret}")
  String defaultUserSecret;

  @BeforeEach
  void setUpEnv(){
    mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    var testUser = "newUser";
    signupRequest = SignupRequest.builder()
        .dob(LocalDate.now())
        .firstName("lorem")
        .lastName("ipsium")
        .email(testUser.concat("@mail.com"))
        .username(testUser)
        .password(Optional.of(defaultUserSecret).orElse("test-secret"))
        .role(Role.STUDENT.name())
        .build();

    signinRequest = SigninRequest
        .builder()
        .password(Optional.of(defaultUserSecret).orElse("test-secret"))
        .username(testUser)
        .build();
  }

  @AfterEach
  void tearDownEnv(){
    User user = userService.findByUserName(signupRequest.getUsername());
    repository.deleteAll();
    userService.deleteUser(user.getId());
  }

  @Test
  @DisplayName("test POST v1/auth/signup")
  void testSignUp_whenValidUserDetailsProvided_returnsCreateUserDetails() throws Exception {
    RequestBuilder signup = MockMvcRequestBuilders.post("/v1/auth/signup")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(signupRequest));

    String signupResponse = authSignup.exec(signup, mockMvc).getResponse().getContentAsString();
    JsonNode node = mapper.readTree(signupResponse);
    User user = mapper.readValue(signupResponse, User.class);

    Assertions.assertEquals(signupRequest.getUsername(), node.get("username").asText());
    Assertions.assertEquals(signupRequest.getEmail(), user.getEmail());
  }

  @Test
  @DisplayName("test POST v1/auth/signin")
  void testSignIn_whenValidUserDetailsProvided_returnsAuthenticationToken() throws Exception {
    RequestBuilder signup = MockMvcRequestBuilders.post("/v1/auth/signup")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(signupRequest));

    String signupResponse = authSignup.exec(signup, mockMvc).getResponse().getContentAsString();
    User user = mapper.readValue(signupResponse, User.class);

    String signinResponse = authSignin.exec(MockMvcRequestBuilders.post("/v1/auth/signin")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(signinRequest)), mockMvc).getResponse().getContentAsString();
    JsonNode response = mapper.readTree(signinResponse);

    Assertions.assertTrue(jwtUtil.validateToken(response.get("access_token").asText(), user));
    Assertions.assertTrue(jwtUtil.validateToken(response.get("refresh_token").asText(), user));
  }

  private final MockAuthRequest<MockMvc, RequestBuilder> authSignin = (request, client) -> {
    try {
      return client.perform(request).andReturn();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  };
  private final MockAuthRequest<MockMvc, RequestBuilder> authSignup = (request, client) -> {
    try {
      return client.perform(request).andReturn();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  };
}