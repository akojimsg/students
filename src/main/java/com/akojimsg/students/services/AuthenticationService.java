package com.akojimsg.students.services;

import com.akojimsg.students.data.dtos.SigninRequest;
import com.akojimsg.students.data.dtos.SigninResponse;
import com.akojimsg.students.data.dtos.SignupRequest;
import com.akojimsg.students.data.entities.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface AuthenticationService {
  User createUser(SignupRequest request);
  SigninResponse authenticateUser(SigninRequest request) throws JsonProcessingException;

  void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
