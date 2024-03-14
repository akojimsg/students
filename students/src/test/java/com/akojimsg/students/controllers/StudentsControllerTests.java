package com.akojimsg.students.controllers;

import com.akojimsg.students.AbstractApplicationContextTest;
import com.akojimsg.students.data.entities.Student;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import java.text.SimpleDateFormat;
import java.util.List;

@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class StudentsControllerTests extends AbstractApplicationContextTest {
  @TestConfiguration
  static class StudentControllerTestsConfiguration {
  }

  @Autowired
  MockMvc mockMvc;

  @Test
  @DisplayName("Test GET /v1/students and receive 200 OK response")
  void getStudents_whenValidRequestIsReceived_returns200Response() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.get("/v1/students")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON);

    MvcResult response = mockMvc.perform(request).andReturn();
    String responseBody = response.getResponse().getContentAsString();
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    List<Student> students = mapper.readValue(responseBody, new TypeReference<List<Student>>() {});
    Assertions.assertEquals(response.getResponse().getStatus(),200);
    Assertions.assertEquals(students.size(), 50);
  }

  @Test
  @DisplayName("Test GET /v1/students/{id} with 200 OK response")
  void getStudentById_whenIdIsValid_returns200Response() throws Exception {

    RequestBuilder request = MockMvcRequestBuilders.get("/v1/students/1")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON);

    MvcResult response = mockMvc.perform(request).andReturn();
    String responseBody = response.getResponse().getContentAsString();

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    Student students = mapper.readValue(responseBody, Student.class);
    Assertions.assertEquals(response.getResponse().getStatus(), 200);
  }

  @Test
  @DisplayName("Test GET /v1/students/{id} with 400 Not Found Exception")
  void getStudentById_whenIdIsValid_returns400Response() throws Exception {

    RequestBuilder request = MockMvcRequestBuilders.get("/v1/students/100")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON);

    MvcResult response = mockMvc.perform(request).andReturn();
    String responseBody = response.getResponse().getContentAsString();

    ObjectMapper mapper = new ObjectMapper();
    Assertions.assertEquals(response.getResponse().getStatus(), 404);
  }
}
