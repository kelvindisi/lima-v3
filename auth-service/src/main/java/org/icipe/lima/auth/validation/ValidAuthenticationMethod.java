package org.icipe.lima.auth.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.icipe.lima.auth.validation.validators.ValidateAuthenticationMethod;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ValidateAuthenticationMethod.class})
public @interface ValidAuthenticationMethod {
  String message() default "{form.validation.valid_authentication_method}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
