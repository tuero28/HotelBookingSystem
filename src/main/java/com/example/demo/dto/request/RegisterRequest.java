package com.example.demo.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
public class RegisterRequest {
    @NotBlank @Size(min = 4, max = 50) private String username;
    @NotBlank @Size(min = 6) private String password;
    @NotBlank private String fullName;
    @Email @NotBlank private String email;
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Invalid phone number") private String phone;
}