package com.liushukov.accounting.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDTO {
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password should have at least 8 characters")
    private String password;
}
