package com.example.web.service;

import com.example.web.dto.request.CreateUserRequest;
import com.example.web.dto.request.UpdateUserRequest;
import com.example.web.dto.reponse.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse create(CreateUserRequest request);
    UserResponse update(Long id, UpdateUserRequest request);
    void delete(Long id);
    List<UserResponse> findAll();
    UserResponse findById(Long id);
}
