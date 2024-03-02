package com.akojimsg.students.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "tb_users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
    }
)
@Getter
@Setter
@NoArgsConstructor
public class UserAccount {
  @Id
  @SequenceGenerator(sequenceName = "user_id_sequence", name = "user_id_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_sequence")
  private Long id;

  @Column
  private String username;

  @Column
  @JsonIgnore
  private String password;

  @ElementCollection(fetch = FetchType.EAGER)
  private List<GrantedAuthority> authorities = new ArrayList<>();

  public UserAccount(String username, String password, String role) {
    this.username = username;
    this.password = password;
    this.authorities.add(new SimpleGrantedAuthority(role));
  }

  public UserDetails asUser() {
    return User
        .withUsername(getUsername())
        .password(getPassword())
        .authorities(getAuthorities())
        .build();
  }
}
