package org.icipe.lima.auth.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.icipe.lima.auth.validation.validators.ValidateGrantType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {ValidateGrantType.class}
)
public @interface ValidGrantType {
    String message() default "{form.validation.valid_grant_type}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
