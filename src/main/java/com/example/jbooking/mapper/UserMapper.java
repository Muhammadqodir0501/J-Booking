package com.example.jbooking.mapper;

import com.example.jbooking.dto.response.UserResponseDto;
import com.example.jbooking.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(User user);
}
