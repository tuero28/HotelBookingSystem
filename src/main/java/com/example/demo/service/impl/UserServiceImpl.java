package com.example.demo.service.impl;

import com.example.demo.dto.request.UpdateUserRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toResponse);
    }

    @Override
    public UserResponse getUserById(Integer id) {
        return userMapper.toResponse(findById(id));
    }

    @Override
    public UserResponse getCurrentUser(String username) {
        return userMapper.toResponse(userRepository.findByUsername(username)
                .orElseThrow(() -> AppException.notFound("User", username)));
    }

    @Override
    @Transactional
    public UserResponse updateUser(Integer id, UpdateUserRequest request) {
        User user = findById(id);
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) throw AppException.notFound("User", id);
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void changePassword(Integer id, com.example.demo.dto.request.ChangePasswordRequest request) {
        User user = findById(id);
        boolean isMatch = passwordEncoder.matches(request.getOldPassword(), user.getPassword()) || 
                          user.getPassword().equals(request.getOldPassword());
        if (!isMatch) {
            throw new RuntimeException("Mật khẩu hiện tại không đúng");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private User findById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> AppException.notFound("User", id));
    }
}