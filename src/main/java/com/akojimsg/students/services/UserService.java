package com.akojimsg.students.services;

import com.akojimsg.students.data.dtos.SignupRequest;
import com.akojimsg.students.data.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
  User createUser(SignupRequest request);
  User findByUserName(String userName);
  List<User> findUsersByFullName(String name);
  List<User> getAllUsers();
  User findUserById(Long id);
  void deleteUser(Long id);
}
