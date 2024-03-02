package com.akojimsg.students.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class StudentDTO {
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String email;

    @NonNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;
}
