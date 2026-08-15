package com.example.web.service;

import com.example.web.dto.reponse.UserRp;
import com.example.web.dto.request.CreateUserRequest;
import com.example.web.dto.request.UpdateUserRequest;

import java.util.List;

public interface UserService {
    void create(CreateUserRequest user);
    void update(Long id, UpdateUserRequest user);
    void delete(Long id);
    List<UserRp> findAll();
}
