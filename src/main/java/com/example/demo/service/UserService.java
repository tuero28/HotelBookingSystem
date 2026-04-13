package com.example.demo.service;

import com.example.demo.dto.request.UpdateUserRequest;
import com.example.demo.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserResponse> getAllUsers(Pageable pageable);
    UserResponse getUserById(Integer id);
    UserResponse getCurrentUser(String username);
    UserResponse updateUser(Integer id, UpdateUserRequest request);
    void changePassword(Integer id, com.example.demo.dto.request.ChangePasswordRequest request);
    void deleteUser(Integer id);
}