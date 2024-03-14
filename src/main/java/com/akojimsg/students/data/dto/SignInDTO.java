package com.akojimsg.students.data.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SignInDTO {

  @NonNull
  private String username;

  @NonNull
  private String password;

}
