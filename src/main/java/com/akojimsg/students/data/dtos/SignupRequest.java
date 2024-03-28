package com.akojimsg.students.data.dtos;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
  private LocalDate dob;
  private String firstName;
  private String lastName;
  private String email;
  private String username;
  private String password;
  private String role;
}
