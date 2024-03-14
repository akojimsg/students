package com.akojimsg.students.services.impl;

import com.akojimsg.students.data.dto.SignUpDTO;
import com.akojimsg.students.data.entities.UserAccount;
import com.akojimsg.students.data.repositories.UserRepository;
import com.akojimsg.students.services.UserManagementService;
import com.akojimsg.students.utils.exceptions.ResourceConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class UserManagementServiceImpl implements UserManagementService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Override
  public UserAccount createUserAccount(SignUpDTO dto) {
    if (userRepository.existsByUsername(dto.getUsername())){
      throw new ResourceConflictException(String.format("Username %s is already registered!", dto.getUsername()));
    };

    UserAccount userAccount = new UserAccount();
    userAccount.setUsername(dto.getUsername());
    userAccount.setPassword(passwordEncoder.encode(dto.getPassword()));
    List<GrantedAuthority> authorityList = new ArrayList<>();
    dto.getPermissions().forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission)));

    userAccount.setAuthorities(authorityList);

    return userRepository.save(userAccount);
  }
}
