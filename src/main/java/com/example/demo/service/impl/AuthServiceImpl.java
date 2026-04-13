package com.example.demo.service.impl;

import com.example.demo.config.JwtTokenProvider;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.response.AuthResponse;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsernameWithRole(request.getUsername())
                .orElseThrow(() -> AppException.unauthorized("Invalid username or password"));

        boolean isMatch = passwordEncoder.matches(request.getPassword(), user.getPassword()) || 
                          user.getPassword().equals(request.getPassword());
        if (!isMatch)
            throw AppException.unauthorized("Invalid username or password");

        String roleName = user.getRole() != null ? user.getRole().getRoleName() : "CUSTOMER";
        return AuthResponse.builder()
                .accessToken(jwtTokenProvider.generateToken(user.getUsername(), roleName))
                .refreshToken(jwtTokenProvider.generateRefreshToken(user.getUsername()))
                .user(userMapper.toResponse(user))
                .build();
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw AppException.conflict("Username already exists");
        if (userRepository.existsByEmail(request.getEmail()))
            throw AppException.conflict("Email already exists");
        if (request.getPhone() != null && userRepository.existsByPhone(request.getPhone()))
            throw AppException.conflict("Phone already exists");

        Role customerRole = roleRepository.findByRoleName("CUSTOMER")
                .orElseThrow(() -> AppException.notFound("Role", "CUSTOMER"));

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(customerRole)
                .build();

        userRepository.save(user);
        return AuthResponse.builder()
                .accessToken(jwtTokenProvider.generateToken(user.getUsername(), "CUSTOMER"))
                .refreshToken(jwtTokenProvider.generateRefreshToken(user.getUsername()))
                .user(userMapper.toResponse(user))
                .build();
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken))
            throw AppException.unauthorized("Invalid refresh token");
        String username = jwtTokenProvider.getUsername(refreshToken);
        User user = userRepository.findByUsernameWithRole(username)
                .orElseThrow(() -> AppException.notFound("User", username));
        String roleName = user.getRole() != null ? user.getRole().getRoleName() : "CUSTOMER";
        return AuthResponse.builder()
                .accessToken(jwtTokenProvider.generateToken(username, roleName))
                .refreshToken(jwtTokenProvider.generateRefreshToken(username))
                .user(userMapper.toResponse(user))
                .build();
    }
}