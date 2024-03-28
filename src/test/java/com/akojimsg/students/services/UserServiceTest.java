package com.akojimsg.students.services;

import com.akojimsg.students.data.entities.User;
import com.akojimsg.students.data.repositories.UserRepository;
import com.akojimsg.students.services.impl.UserServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.*;

import static org.mockito.BDDMockito.given;

@SpringBootTest
@ActiveProfiles("test")
@Import(UserServiceImpl.class)
public class UserServiceTest {
  static List<User> usersList;

  @Configuration
  static class UserServiceTestConfiguration {
  }

  @BeforeAll
  static void loadStudentData() {
    try (InputStream inputStream = TypeReference.class.getResourceAsStream("/static/users.json")) {
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
      usersList = mapper.readValue(inputStream, new TypeReference<List<User>>() {});
    } catch (IOException e) {
      usersList = new ArrayList<>();
    }
  }

  @Autowired
  UserService userService;
  @MockBean
  private UserRepository userRepository;
  @MockBean
  private PasswordEncoder passwordEncoder;

  @DisplayName("Test method getAllUsers")
  @Test
  void testGetAllStudents_returnsListOfAllUsers(){
    given(userRepository.findAll()).willReturn(usersList);

    Assertions.assertThat(userService.getAllUsers().size()).isEqualTo(20);
  }

  @DisplayName("Test method findUserById")
  @ParameterizedTest
  @MethodSource("generateIds")
  void testGetStudentById_withValidId_returnsStudentWithMatchingId(int id){
    given(userRepository.findById((long) id)).willReturn(Optional.ofNullable(findById((long) id)));

    User found = userService.findUserById((long) id);
    User expected = usersList.get(id-1);

    Assertions.assertThat(found).isEqualTo(expected);
  }

  @DisplayName("Test method findUsersByName")
  @ParameterizedTest
  @MethodSource("generateArgsOfNamesToSearch")
  void testGetStudentByName_withNameToSearch_returnsMatchingStudents(String name, int occurrence){
    given(userRepository.findByFullNameContains(name)).willReturn(findByFullName(name));

    List<User> found = userService.findUsersByFullName(name);

    Assertions.assertThat(found.size()).isEqualTo(occurrence);
  }

  public static User findById(Long id) {
    return usersList
        .stream()
        .filter(student -> Objects.equals(student.getId(), id))
        .findAny()
        .orElse(null);
  }

  public static List<User> findByFullName(String name) {
    return usersList
        .stream()
        .filter(user -> user
            .getFullName()
            .contains(name))
        .collect(Collectors.toList());
  }

  public static Stream<Arguments> generateIds() {
    return IntStream.rangeClosed(1,10).mapToObj(Arguments::of);
  }

  public static Stream<Arguments> generateArgsOfNamesToSearch(){
    List<String> params = List.of("J,1", "Ma,3", "Armstrong,1", "S,1", "Kevin,0");
    return params
        .stream()
        .map(param -> {
          String[] args = param.split(",");
          return Arguments.of(args[0], Integer.valueOf(args[1]));
        });
  }
}
