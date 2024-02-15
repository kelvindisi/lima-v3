package org.icipe.lima.auth.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.icipe.lima.auth.helper.AuthorizationUtils;
import org.icipe.lima.auth.validation.ValidGrantType;

import java.util.Map;

public class ValidateGrantType implements ConstraintValidator<ValidGrantType, String[]> {
  @Override
  public boolean isValid(String[] types, ConstraintValidatorContext constraintValidatorContext) {
    Map<String, String> grantTypes = AuthorizationUtils.getGrantTypes();
    for (String type : types) {
      if (!grantTypes.containsValue(type)) return false;
    }
    return true;
  }
}
