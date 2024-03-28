package com.akojimsg.students.services.impl;

import com.akojimsg.students.data.dtos.SigninRequest;
import com.akojimsg.students.data.dtos.SigninResponse;
import com.akojimsg.students.data.dtos.SignupRequest;
import com.akojimsg.students.data.entities.Token;
import com.akojimsg.students.data.entities.User;
import com.akojimsg.students.data.repositories.TokenRepository;

import com.akojimsg.students.services.AuthenticationService;
import com.akojimsg.students.services.UserService;
import com.akojimsg.students.utils.auth.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.akojimsg.students.utils.auth.TokenType.BEARER_TOKEN;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
  private final UserService userService;
  @Autowired
  private TokenRepository tokenRepository;

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;
  @Override
  public User createUser(SignupRequest request) {
    return userService.createUser(request);
  }

  @Override
  public SigninResponse authenticateUser(SigninRequest request) throws JsonProcessingException {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
        )
    );
    var user = userService.findByUserName(request.getUsername());
    var accessToken = jwtUtil.generateAccessToken(user);
    var refreshToken = jwtUtil.generateRefreshToken(user);
    revokeAllUserTokens.accept(user);
    saveUserToken.accept(user, accessToken);
    return SigninResponse
        .builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  @Override
  public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    String username = null;
    String refreshToken = null;

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      refreshToken = authorizationHeader.split(" ")[1];
      username = jwtUtil.extractUsername(refreshToken);
    }

    if (username != null) {
      User user = Optional.of(this.userService.findByUserName(username)).orElseThrow();
//      var isTokenValid = tokenRepository.findByToken(refreshToken)
//          .map(token -> !token.isExpired() && !token.isRevoked())
//          .orElse(false);
//      if (jwtUtil.validateToken(refreshToken, userDetails) && isTokenValid)
      if (jwtUtil.validateToken(refreshToken, user)) {
        var accessToken = jwtUtil.generateAccessToken(user);
        revokeAllUserTokens.accept(user);
        saveUserToken.accept(user, accessToken);
        var signinResponse = SigninResponse
            .builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), signinResponse);
      }
    }
  }

  private final BiConsumer<User, String> saveUserToken = (user, token) -> tokenRepository.save(
      Token
        .builder()
        .user(user)
        .token(token)
        .tokenType(BEARER_TOKEN)
        .expired(false)
        .revoked(false)
        .build()
  );

  private final Consumer<User> revokeAllUserTokens = (user) -> {
    var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
    if(validUserTokens.isEmpty()) return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  };
}
