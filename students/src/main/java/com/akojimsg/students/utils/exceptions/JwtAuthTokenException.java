package com.akojimsg.students.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class JwtAuthTokenException extends RuntimeException{
  public JwtAuthTokenException(String message) {
    super(message);
  }
}
