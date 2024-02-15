package org.icipe.lima.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAccount(
    @NotBlank @Size(min = 4, message = "{form.validation.size}") String firstName,
    @NotBlank @Size(min = 4, message = "{form.validation.size}") String lastName,
    @NotBlank @Email String email,
    @NotBlank @Size(min = 8, message = "{form.validation.size}") String password) {}
