package com.example.demo.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
public class UpdateUserRequest {
    @NotBlank private String fullName;
    @Email private String email;
    @Pattern(regexp = "^[0-9]{10,11}$") private String phone;
}