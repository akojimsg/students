package com.akojimsg.students.utils.configs;

import com.akojimsg.students.data.entities.Student;
import com.akojimsg.students.data.repositories.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.javafaker.*;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Locale;

@Configuration
public class StudentConfig {
    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository) {
        return args -> {
            Faker faker = new Faker(new Locale("en-US"));
            for (int i = 0; i < 50; i++) {
                Student s = new Student();
                s.setName(faker.name().fullName());
                s.setEmail(faker.internet().emailAddress());
                s.setDob(
                        Instant.ofEpochMilli(faker.date().birthday(0, 120).getTime())
                                .atZone(ZoneId.systemDefault()).toLocalDate()
                );
                studentRepository.save(s);
            }
        };
    }
}
