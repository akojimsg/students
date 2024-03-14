package com.akojimsg.students.utils.configs;

import com.akojimsg.students.data.entities.Student;
import com.akojimsg.students.data.repositories.StudentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

@Configuration
public class StudentConfig {
  private static final Logger logger = LoggerFactory.getLogger(StudentConfig.class);
  @Bean
  CommandLineRunner commandLineRunner(StudentRepository studentRepository) {
    return args -> {
      logger.info("Loading initial data from json file");
      try (InputStream inputStream = TypeReference.class.getResourceAsStream("/static/students.json")) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        List<Student> students = mapper.readValue(inputStream, new TypeReference<List<Student>>() {});
        studentRepository.saveAll(students);
        logger.info("Initial data loaded successfully!");
      } catch (IOException e) {
        logger.error("Failed to load initial data: {0}", e);
      }
    };
  }
}
