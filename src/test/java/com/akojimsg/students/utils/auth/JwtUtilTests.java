package com.akojimsg.students.utils.auth;

import com.akojimsg.students.data.entities.UserAccount;
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
    testUser = new UserAccount("test", "password", "ROLE_USER").asUser();
  }

  @DisplayName("Test token is valid")
  @ParameterizedTest
  @MethodSource("generateUsersDetailsWithTokens")
  void testValidateToken_whenTokenIsValid_returnsTrue(UserDetails user) {
    String token = jwtUtil.generateToken(user);
    Assertions.assertTrue(jwtUtil.validateToken(token, user), "Token is valid");
  }

  @DisplayName("Test token is invalid")
  @ParameterizedTest
  @MethodSource("generateUsersList")
  void testValidateToken_whenTokenIsInValid_returnsFalse(UserDetails arg) {
    String token = jwtUtil.generateToken(testUser);
    Assertions.assertFalse(jwtUtil.validateToken(token, arg), "Token is invalid");
  }

  private static List<UserAccount> generateTestUserAccounts(int n) {
    List<UserAccount> userAccountList = new ArrayList<>();

    for(int i = 0; i < n; i++)
      userAccountList.add(new UserAccount("test"+i, "password"+i, "ROLE_TEST"));

    return userAccountList;
  }

  private static Stream<Arguments> generateUsersList() {
    return generateTestUserAccounts(new Random().nextInt(100))
        .stream()
        .map(userAccount -> Arguments.of(userAccount.asUser()));
  }

  public static Stream<Arguments> generateUsersDetailsWithTokens() {
    return generateTestUserAccounts(new Random().nextInt(100))
        .stream()
        .map(userAccount -> Arguments.of(userAccount.asUser()));
  }
}
