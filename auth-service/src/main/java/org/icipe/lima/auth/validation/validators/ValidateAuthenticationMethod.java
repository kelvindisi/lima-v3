package org.icipe.lima.auth.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Map;
import org.icipe.lima.auth.helper.AuthorizationUtils;
import org.icipe.lima.auth.validation.ValidAuthenticationMethod;

public class ValidateAuthenticationMethod
    implements ConstraintValidator<ValidAuthenticationMethod, String[]> {
  @Override
  public boolean isValid(String[] methods, ConstraintValidatorContext constraintValidatorContext) {
    Map<String, String> authenticationMethods = AuthorizationUtils.getAuthenticationMethods();
    for (String method : methods) {
      if (!authenticationMethods.containsValue(method)) return false;
    }
    return true;
  }
}
