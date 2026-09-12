package com.example.web.mapper;

import com.example.web.dto.request.CreateUserRequest;
import com.example.web.dto.reponse.UserResponse;
import com.example.web.model.Role;
import com.example.web.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    public User toEntity(CreateUserRequest request, String encodedPassword, Role role) {
        return User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(encodedPassword)
                .avatarUrl(request.getAvatarUrl() != null ? request.getAvatarUrl() : "")
                .role(role)
                .status(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .gender(user.getGender())
                .dateOfBirth(user.getDateOfBirth())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .roleId(user.getRole() != null ? user.getRole().getId() : null)
                .roleName(user.getRole() != null ? user.getRole().getName() : null)
                .status(user.isStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
