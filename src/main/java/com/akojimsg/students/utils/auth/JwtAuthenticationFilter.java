package com.akojimsg.students.utils.auth;

import com.akojimsg.students.data.entities.Token;
import com.akojimsg.students.data.repositories.TokenRepository;
import com.akojimsg.students.services.AuthenticationService;
import com.akojimsg.students.utils.exceptions.JwtAuthTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
  private final JwtUtil jwtUtil;
  private final UserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws JwtAuthTokenException {
    try {
      final String authorizationHeader = request.getHeader("Authorization");
      String username = null;
      String jwt = null;

      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        jwt = authorizationHeader.split(" ")[1];
        username = jwtUtil.extractUsername(jwt);
      }

      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        var isTokenValid = tokenRepository.findByToken(jwt)
            .map(token -> !token.isExpired() && !token.isRevoked())
            .orElse(false);
        if (jwtUtil.validateToken(jwt, userDetails) && isTokenValid) {
          UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
              userDetails,
              null,
              userDetails.getAuthorities()
          );
          usernamePasswordAuthenticationToken
              .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder
              .getContext()
              .setAuthentication(usernamePasswordAuthenticationToken);
        }
      }

      filterChain.doFilter(request, response);
    } catch (Exception e) {
      logger.error("Cannot set user authentication: {0}", e);
      throw new JwtAuthTokenException(e.getMessage());
    }
  }

}
