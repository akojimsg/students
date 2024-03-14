package com.akojimsg.students.data.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDTO {

  @NonNull
  private String username;

  @NonNull
  private String password;

  private List<String> permissions;
}
