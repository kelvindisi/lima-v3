package org.icipe.lima.auth.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.icipe.lima.auth.validation.validators.ValidateClientId;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ValidateClientId.class})
public @interface ValidClientId {
    String message() default "{form.validation.unavailable}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
