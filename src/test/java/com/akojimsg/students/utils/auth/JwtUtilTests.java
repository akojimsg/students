package com.akojimsg.students.utils.auth;

import com.akojimsg.students.data.entities.Role;
import com.akojimsg.students.data.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@SpringBootTest
@ActiveProfiles("test")
public class JwtUtilTests {

  @Configuration
  static class JwtUtilTestsConfiguration {
    @Bean
    JwtUtil createJwtUtilObj() {
      return new JwtUtil();
    };
  }

  UserDetails testUser;
  @Autowired
  JwtUtil jwtUtil;

  @BeforeEach
  void init(){
    testUser = User
        .builder()
        .dob(LocalDate.now())
        .firstName("Test")
        .lastName("User")
        .email("test_user@mail.com")
        .password("test-secret")
        .username("test_user")
        .role(Role.ADMIN)
        .build();
  }

  @DisplayName("Test token is valid")
  @ParameterizedTest
  @MethodSource("generateUsersDetailsWithTokens")
  void testValidateToken_whenTokenIsValid_returnsTrue(UserDetails user) {
    String token = jwtUtil.generateAccessToken(user);
    Assertions.assertTrue(jwtUtil.validateToken(token, user), "Token is valid");
  }

  @DisplayName("Test token is invalid")
  @ParameterizedTest
  @MethodSource("generateUsersList")
  void testValidateToken_whenTokenIsInValid_returnsFalse(UserDetails arg) {
    String token = jwtUtil.generateAccessToken(testUser);
    Assertions.assertFalse(jwtUtil.validateToken(token, arg), "Token is invalid");
  }

  private static List<User> generateTestUserAccounts(int n) {
    List<User> users = new ArrayList<>();

    for(int i = 0; i < n; i++)
      users.add(User
          .builder()
          .id((long) i)
          .username("test"+i)
          .email("test"+i+"@mail.com")
          .password("test-secret")
          .firstName("test")
          .lastName("user")
          .dob(LocalDate.now())
          .role(Role.STAFF)
          .build()
      );
      //users.add(new UserAccount("test"+i, "password"+i, "ROLE_TEST"));

    return users;
  }

  private static Stream<Arguments> generateUsersList() {
    return generateTestUserAccounts(new Random().nextInt(100))
        .stream()
        .map(Arguments::of);
  }

  public static Stream<Arguments> generateUsersDetailsWithTokens() {
    return generateTestUserAccounts(new Random().nextInt(100))
        .stream()
        .map(Arguments::of);
  }
}
