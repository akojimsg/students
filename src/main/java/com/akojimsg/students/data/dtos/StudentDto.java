package com.akojimsg.students.data.dtos;

import com.akojimsg.students.data.entities.Role;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudentDto extends SignupRequest {
    private final String role = Role.STUDENT.name();
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;
}
