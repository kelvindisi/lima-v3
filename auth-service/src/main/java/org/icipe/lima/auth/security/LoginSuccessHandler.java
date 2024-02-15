package org.icipe.lima.auth.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
    System.out.println("Authorities: " + authorities);

    if (authorities.contains("ROLE_SUPER_ADMIN")) {
      response.sendRedirect("/clients");
    } else {
      //        should redirect to user portal account to check -> authentication history
      response.sendRedirect("/");
    }
  }
}
