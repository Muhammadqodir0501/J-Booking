package com.example.jbooking.controller;

import com.example.jbooking.dto.response.AuthResponseDto;
import com.example.jbooking.dto.response.UserResponseDto;
import com.example.jbooking.exception.ApiResponse;
import com.example.jbooking.security.SecurityUtils;
import com.example.jbooking.service.abstaction.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/become-admin")
    public ResponseEntity<ApiResponse<AuthResponseDto>> becomeAdmin() {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(new ApiResponse<>(userService.becomeAdmin(currentUserId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUsers() {
        return ResponseEntity.ok(new ApiResponse<>(userService.getAllUsers()));
    }
}