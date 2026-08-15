package com.example.web.service.implement;

import com.example.web.dto.reponse.UserRp;
import com.example.web.dto.request.CreateUserRequest;
import com.example.web.dto.request.UpdateUserRequest;
import com.example.web.model.User;
import com.example.web.repository.UserReponsitory;
import com.example.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IUserService implements UserService {
    private final UserReponsitory userReponsitory;
    private PasswordEncoder passwordEncoder;

    @Override
    public void create(CreateUserRequest userRq) {
        userReponsitory.save(User.builder()
                .fullName(userRq.getFullName())
                .email(userRq.getEmail())
                .phone(userRq.getPhone())
                .passwordHash(passwordEncoder.encode(userRq.getPassword()))
                .avatarUrl("")
                .role(null)
                .status(true)
                .createdAt(LocalDateTime.now())
                .build());

    }

    @Override
    public void update(Long id, UpdateUserRequest userRq) {
        Optional<User> user = userReponsitory.findById(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        User userUpdate = user.get();
        userUpdate.setFullName(userRq.getFullName());
        userUpdate.setPhone(userRq.getPhone());
        userUpdate.setEmail(userRq.getEmail());
        userUpdate.setAvatarUrl(userRq.getAvatarUrl());
        userReponsitory.save(userUpdate);
    }

    @Override
    public void delete(Long id) {
        userReponsitory.deleteById(id);
    }

    @Override
    public List<UserRp> findAll() {
        List<User> users = userReponsitory.findAll();
        return List.of();
    }
}
