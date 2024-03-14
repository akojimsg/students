package com.akojimsg.students.controllers;

import com.akojimsg.students.data.dto.*;
import com.akojimsg.students.data.entities.UserAccount;
import com.akojimsg.students.services.UserManagementService;
import com.akojimsg.students.utils.auth.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private UserDetailsService userDetailsService;
  @Autowired
  private UserManagementService userManagementService;
  @Autowired
  private JwtUtil jwtUtil;

  @PostMapping(path = "signin")
  public ResponseEntity<Map<String, String>> generateJwtToken(@Valid @RequestBody SignInDTO userDTO) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetails userDetails = userDetailsService.loadUserByUsername(userDTO.getUsername());
    String token = jwtUtil.generateToken(userDetails);
    Map<String, String> response = new HashMap<>();
    response.put("token", token);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping(path = "signup")
  public ResponseEntity<UserAccount> registerUser(@Valid @RequestBody SignUpDTO dto) {
    UserAccount responseObject = userManagementService.createUserAccount(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseObject);
  }
}
