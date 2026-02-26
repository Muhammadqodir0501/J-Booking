package com.example.jbooking.service.abstaction;

import com.example.jbooking.dto.request.UserLoginRequestDto;
import com.example.jbooking.dto.request.UserRegisterRequestDto;
import com.example.jbooking.dto.response.AuthResponseDto;
import com.example.jbooking.dto.response.UserResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface UserService {
    @Transactional
    AuthResponseDto registerUser(UserRegisterRequestDto request);

    @Transactional
    AuthResponseDto becomeAdmin(UUID adminId);

    AuthResponseDto login(UserLoginRequestDto request);

    List<UserResponseDto> getAllUsers();
}
