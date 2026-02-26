package com.example.jbooking.controller;

import com.example.jbooking.dto.request.UserLoginRequestDto;
import com.example.jbooking.dto.request.UserRegisterRequestDto;
import com.example.jbooking.dto.response.AuthResponseDto;
import com.example.jbooking.exception.ApiResponse;
import com.example.jbooking.service.abstaction.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponseDto>> register(@RequestBody @Valid UserRegisterRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(userService.registerUser(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@RequestBody @Valid UserLoginRequestDto request) {
        return ResponseEntity.ok(new ApiResponse<>(userService.login(request)));
    }
}