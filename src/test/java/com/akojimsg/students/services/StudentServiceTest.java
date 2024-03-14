package com.akojimsg.students.services;

import com.akojimsg.students.data.entities.Student;
import com.akojimsg.students.data.repositories.StudentRepository;
import com.akojimsg.students.services.impl.StudentServiceImpl;
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
@Import(StudentServiceImpl.class)
public class StudentServiceTest {
  static List<Student> studentDb;

  @Configuration
  static class StudentServiceTestConfiguration {
  }

  @BeforeAll
  static void loadStudentData() {
    try (InputStream inputStream = TypeReference.class.getResourceAsStream("/static/students.json")) {
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
      studentDb = mapper.readValue(inputStream, new TypeReference<List<Student>>() {});
    } catch (IOException e) {
      studentDb = new ArrayList<>();
    }
  }

  @Autowired
  StudentService studentService;

  @MockBean
  private StudentRepository studentRepository;

  @DisplayName("Test method getAllStudents")
  @Test
  void testGetAllStudents_returnsListOfAllStudents(){
    given(studentRepository.findAll()).willReturn(studentDb);

    Assertions.assertThat(studentService.getAllStudents().size()).isEqualTo(50);
  }

  @DisplayName("Test method findStudentById")
  @ParameterizedTest
  @MethodSource("generateIds")
  void testGetStudentById_withValidId_returnsStudentWithMatchingId(int id){
    given(studentRepository.findById((long) id)).willReturn(Optional.ofNullable(findById((long) id)));

    Student found = studentService.findStudentById((long) id);
    Student expected = studentDb.get(id-1);

    Assertions.assertThat(found).isEqualTo(expected);
  }

  @DisplayName("Test method findStudentsByName")
  @ParameterizedTest
  @MethodSource("generateArgsOfNamesToSearch")
  void testGetStudentByName_withNameToSearch_returnsMatchingStudents(String name, int occurrence){
    given(studentRepository.findByNameContains(name)).willReturn(findByName(name));

    List<Student> found = studentService.findStudentsByName(name);

    Assertions.assertThat(found.size()).isEqualTo(occurrence);
  }

  public static Student findById(Long id) {
    return studentDb
        .stream()
        .filter(student -> Objects.equals(student.getId(), id))
        .findAny()
        .orElse(null);
  }

  public static List<Student> findByName(String name) {
    return studentDb
        .stream()
        .filter(student -> student.getName().contains(name))
        .collect(Collectors.toList());
  }

  public static Stream<Arguments> generateIds() {
    return IntStream.rangeClosed(1,10).mapToObj(Arguments::of);
  }

  public static Stream<Arguments> generateArgsOfNamesToSearch(){
    List<String> params = List.of("John,1", "Myrtle,1", "Miss,5", "T,6", "Kevin,1");
    return params
        .stream()
        .map(param -> {
          String[] args = param.split(",");
          return Arguments.of(args[0], Integer.valueOf(args[1]));
        });
  }

}
