package org.icipe.lima.auth.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.icipe.lima.auth.entity.user.AppUser;
import org.icipe.lima.auth.repository.user.UserRepository;
import org.icipe.lima.auth.validation.ValidUserEmail;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ValidateUserEmail implements ConstraintValidator<ValidUserEmail, String> {
  private final UserRepository userRepository;

  @Override
  public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
    Optional<AppUser> user = userRepository.findByEmail(email);

    return user.isEmpty();
  }
}
