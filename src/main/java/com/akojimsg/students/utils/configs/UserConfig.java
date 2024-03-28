package com.akojimsg.students.utils.configs;

import com.akojimsg.students.data.entities.User;
import com.akojimsg.students.data.repositories.StudentRepository;
import com.akojimsg.students.data.repositories.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class UserConfig {
  private static final Logger logger = LoggerFactory.getLogger(UserConfig.class);
  private final PasswordEncoder passwordEncoder;

  @Value("${application.settings.default.userinfo-secret}")
  private String defaultUserSecret;

  @Bean
  CommandLineRunner commandLineRunner(UserRepository repository) {
    return args -> {
      logger.info("Loading initial data from json file");
      try (InputStream inputStream = TypeReference.class.getResourceAsStream("/static/users.json")) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        List<User> users = mapper.readValue(inputStream, new TypeReference<List<User>>() {})
            .stream()
            .peek((user) -> user.setPassword(passwordEncoder.encode(Optional.of(defaultUserSecret).orElse("test-secret")))).collect(Collectors.toList());
        repository.saveAll(users);
        logger.info("Initial data loaded successfully!");
      } catch (IOException e) {
        logger.error("Failed to load initial data: {0}", e);
      }
    };
  }
}
