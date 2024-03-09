package org.icipe.lima.auth.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.icipe.lima.auth.validation.ValidUserEmail;

public record CreateUserAccount(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotBlank @Email @ValidUserEmail String email,
    @NotBlank
        @Digits(integer = 10, fraction = 0, message = "${form.validation.phoneNumber}")
        @Positive
        String phoneNumber,
    @NotBlank @Size(min = 8, message = "{form.validation.size}") String password) {}
