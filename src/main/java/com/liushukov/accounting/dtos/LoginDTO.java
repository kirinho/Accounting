package com.liushukov.accounting.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDTO(
        @Email(message = "Email should be valid")
        @NotBlank(message = "Email is mandatory")
        String email,
        @NotBlank(message = "Password is mandatory")
        @Size(min = 8, message = "Password should have at least 8 characters")
        String password
) {}
