package com.akojimsg.students.data.repositories;

import com.akojimsg.students.data.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByNameContains(String name);
    Optional<Student> findByEmail(String email);
}
