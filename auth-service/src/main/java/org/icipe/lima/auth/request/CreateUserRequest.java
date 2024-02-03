package org.icipe.lima.auth.request;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String email,
        @NotBlank String phoneNumber,
        @NotBlank String password
) {}
