package com.akojimsg.students.data.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class SigninResponse {
  @JsonProperty("access_token")
  private final String accessToken;

  @JsonProperty("refresh_token")
  private final String refreshToken;
}
