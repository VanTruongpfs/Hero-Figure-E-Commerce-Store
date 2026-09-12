package com.example.web.service;

import com.example.web.dto.reponse.RoleResponse;
import com.example.web.dto.request.CreateRoleRequest;

public interface RoleService {
    RoleResponse create(CreateRoleRequest request);
}
