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

  @DisplayName("Test GET /students")
  @Test
  void testGetAllStudents_withValidInput_returnsOkResponse(){
    given(studentRepository.findAll()).willReturn(studentDb);

    Assertions.assertThat(studentService.getAllStudents().size()).isEqualTo(50);
  }
}
