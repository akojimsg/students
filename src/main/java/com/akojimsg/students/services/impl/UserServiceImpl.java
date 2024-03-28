package com.akojimsg.students.services.impl;

import com.akojimsg.students.data.dtos.SignupRequest;
import com.akojimsg.students.data.entities.Role;
import com.akojimsg.students.data.entities.User;
import com.akojimsg.students.data.repositories.UserRepository;
import com.akojimsg.students.services.UserService;
import com.akojimsg.students.utils.exceptions.ResourceConflictException;
import com.akojimsg.students.utils.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;

  public List<User> getAllUsers(){
    return this.repository.findAll();
  }

  public List<User> findUsersByFullName(String name){
    return repository.findByFullNameContains(name);
  }

  public User findUserById(Long id){
    return repository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException(String.format("Student not found with id %d", id))
    );
  }

  @Override
  public User createUser(SignupRequest request) {
    if (repository.existsByUsername(request.getUsername())){
      throw new ResourceConflictException(String.format("Username %s is already registered!", request.getUsername()));
    };
    var user = User.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .dob(request.getDob())
        .username(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.valueOf(request.getRole()))
        .build();
    return repository.save(user);
  }

  @Override
  public User findByUserName(String userName) {
    return repository.findByUsername(userName).orElseThrow(
        () -> new ResourceNotFoundException(String.format("No user with username: %s", userName))
    );
  }

  @Override
  public void deleteUser(Long id){
    if(!repository.existsById(id))
      throw new ResourceNotFoundException(String.format("User not found with id %d", id));
    repository.deleteById(id);
  }
}
