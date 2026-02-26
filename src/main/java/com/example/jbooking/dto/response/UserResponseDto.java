package com.example.jbooking.dto.response;

import com.example.jbooking.enums.Role;
import lombok.Data;

import java.util.UUID;

@Data
public class UserResponseDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}
