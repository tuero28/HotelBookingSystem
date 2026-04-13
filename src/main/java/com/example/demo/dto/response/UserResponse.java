package com.example.demo.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class UserResponse {
    private Integer userId;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String roleName;
    private LocalDateTime createdAt;
}