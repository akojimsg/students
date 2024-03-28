package com.akojimsg.students.controllers;

import com.akojimsg.students.data.dtos.*;
import com.akojimsg.students.data.entities.User;
import com.akojimsg.students.services.AuthenticationService;
import com.akojimsg.students.utils.exceptions.JwtAuthTokenException;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.io.IOException;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping(path = "signin")
  public ResponseEntity<SigninResponse> signin(@RequestBody SigninRequest request) {
    SigninResponse response = null;
    try {
      response = service.authenticateUser(request);
    } catch (JsonProcessingException e) {
      throw new JwtAuthTokenException(e.getMessage());
    }
    return ResponseEntity.ok(response);
  }

  @PostMapping(path = "signup")
  public ResponseEntity<User> signup(@RequestBody SignupRequest request) {
    return ResponseEntity.ok(service.createUser(request));
  }

  @PostMapping(path = "refresh-token")
  public void refreshAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    service.refreshToken(request, response);
  }
}
