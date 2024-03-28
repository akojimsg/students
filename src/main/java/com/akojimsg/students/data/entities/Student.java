package com.akojimsg.students.data.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@Data
@Entity
@Table(name = "tb_student", uniqueConstraints={@UniqueConstraint(columnNames = {"id", "email"})})
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class Student extends User {
    @Id
    @SequenceGenerator(sequenceName = "id_sequence", name = "id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
    private Long id;

    private final Role role = Role.STUDENT;
}

/*
* Associate students with an institution
* Create Departments
* Create Courses
* Create Subjects
* */