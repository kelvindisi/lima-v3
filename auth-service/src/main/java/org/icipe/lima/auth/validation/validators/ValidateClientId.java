package org.icipe.lima.auth.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.icipe.lima.auth.repository.client.ClientRepository;
import org.icipe.lima.auth.validation.ValidClientId;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidateClientId implements ConstraintValidator<ValidClientId, String> {
    private final ClientRepository clientRepository;

    @Override
    public boolean isValid(String clientId, ConstraintValidatorContext constraintValidatorContext) {
        int count = clientRepository.countByClientId(clientId);
        return count == 0;
    }
}
