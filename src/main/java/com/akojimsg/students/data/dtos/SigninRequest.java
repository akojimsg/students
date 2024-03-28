package com.akojimsg.students.data.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SigninRequest {
  private String username;
  private String password;
}
