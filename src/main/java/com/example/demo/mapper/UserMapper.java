package com.example.demo.mapper;

import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(User user) {
        if (user == null) return null;
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .roleName(user.getRole() != null ? user.getRole().getRoleName() : null)
                .createdAt(user.getCreatedAt())
                .build();
    }
}