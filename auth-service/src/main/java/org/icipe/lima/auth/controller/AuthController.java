package org.icipe.lima.auth.controller;

import jakarta.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.icipe.lima.auth.request.CreateAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/auth")
public class AuthController {
  @GetMapping("/register")
  public String registerUser(@ModelAttribute("createAccount") CreateAccount createAccount) {
    return "auth/create_account";
  }

  @PostMapping("/register")
  public String createUserAccount(
      @Valid @ModelAttribute("createAccount") CreateAccount createAccount,
      Errors errors,
      RedirectAttributes redirectAttributes) {
    if (errors.hasErrors()) {
      return "auth/create_account";
    }

    redirectAttributes.addFlashAttribute(
        "message", "Account created successfully, check email for validation");

    return "redirect:/auth/register";
  }

  @GetMapping("/login")
  public String login() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication.isAuthenticated()) {
      Set<String> authorities =
          authentication.getAuthorities().stream()
              .map(GrantedAuthority::getAuthority)
              .collect(Collectors.toSet());
      if (authorities.contains("ROLE_SUPER_ADMIN")) {
        return "redirect:/clients";
      }
    }
    return "auth/login";
  }

  @GetMapping("/logout")
  public String logout() {
    return "auth/logout";
  }
}
