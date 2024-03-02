package com.akojimsg.students.data.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "tb_student", uniqueConstraints={@UniqueConstraint(columnNames = {"id", "email"})})
@Getter
@Setter
public class Student {
    @Id
    @SequenceGenerator(sequenceName = "id_sequence", name = "id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Column(nullable = false)
    private LocalDate dob;

    @Column
    @Transient
    private Integer age;

    public Integer getAge() {
        return Period.between(this.getDob(), LocalDate.now()).getYears();
    }
}
