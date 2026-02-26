package com.example.jbooking.service.impl;

import com.example.jbooking.dto.request.UserLoginRequestDto;
import com.example.jbooking.dto.request.UserRegisterRequestDto;
import com.example.jbooking.dto.response.AuthResponseDto;
import com.example.jbooking.dto.response.UserResponseDto;
import com.example.jbooking.entity.User;
import com.example.jbooking.enums.Role;
import com.example.jbooking.exception.ConflictException;
import com.example.jbooking.exception.NotFoundException;
import com.example.jbooking.mapper.UserMapper;
import com.example.jbooking.repository.UserRepository;
import com.example.jbooking.security.JwtService;
import com.example.jbooking.security.SecurityUser;
import com.example.jbooking.service.abstaction.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public AuthResponseDto registerUser(UserRegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Пользователь с таким email уже существует");
        }

        User newUser = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.USER)
                .build();

        userRepository.save(newUser);

        String jwtToken = jwtService.generateToken(new SecurityUser(newUser));

        return AuthResponseDto.builder()
                .token(jwtToken)
                .email(newUser.getEmail())
                .userId(newUser.getId())
                .role(newUser.getRole())
                .build();
    }

    @Transactional
    @Override
    public AuthResponseDto becomeAdmin(UUID adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        String freshJwtToken = jwtService.generateToken(new SecurityUser(admin));

        return AuthResponseDto.builder()
                .token(freshJwtToken)
                .email(admin.getEmail())
                .userId(admin.getId())
                .role(admin.getRole())
                .build();
    }

    @Override
    public AuthResponseDto login(UserLoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        String jwtToken = jwtService.generateToken(new SecurityUser(user));

        return AuthResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .token(jwtToken)
                .role(user.getRole())
                .build();
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return  userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

}
