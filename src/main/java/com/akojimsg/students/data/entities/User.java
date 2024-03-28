package com.akojimsg.students.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "tb_users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
    }
)
public class User implements UserDetails {
  @Id
  @SequenceGenerator(sequenceName = "user_id_sequence", name = "user_id_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_sequence")
  private Long id;

  @Column
  protected String username;

  @Column
  @JsonIgnore
  protected String password;

  @Column
  @Email
  protected String email;

  @Column
  protected String firstName;

  @Column
  protected String fullName;

  @Column
  protected String lastName;

  @Column
  @Enumerated(EnumType.STRING)
  protected Role role;


  @OneToMany(mappedBy = "user")
  @JsonIgnore
  protected List<Token> tokens;

  @Column(nullable = false)
  protected LocalDate dob;

  @Column
  @Transient
  protected Integer age;

  public Integer getAge() {
    return Period.between(this.dob, LocalDate.now()).getYears();
  }

  public String getFullName() {return this.firstName.concat(" ").concat(this.lastName); }

  @Override
  @JsonIgnore
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  @JsonIgnore
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  @JsonIgnore
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  @JsonIgnore
  public boolean isEnabled() {
    return true;
  }
}
