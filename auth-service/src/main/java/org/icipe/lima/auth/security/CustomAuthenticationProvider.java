package org.icipe.lima.auth.security;

import lombok.RequiredArgsConstructor;
import org.icipe.lima.auth.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    var email = authentication.getName();
    var password = authentication.getCredentials().toString();

    var securityUser = userService.loadUserByUsername(email);

    if (!verifyUserPassword(password, securityUser.getPassword())) {
      throw new BadCredentialsException("wrong login credentials");
    }

    return new UsernamePasswordAuthenticationToken(
            securityUser.getUsername(),
            securityUser.getPassword(),
            securityUser.getAuthorities()
    );
  }

  private boolean verifyUserPassword(String rawPassword, String encodedPassword) {
    return passwordEncoder.matches(rawPassword, encodedPassword);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
