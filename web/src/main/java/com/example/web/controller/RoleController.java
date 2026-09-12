package com.example.web.controller;

import com.example.web.dto.reponse.ApiResponse;
import com.example.web.dto.reponse.RoleResponse;
import com.example.web.dto.request.CreateRoleRequest;
import com.example.web.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> create(
            @Valid @RequestBody CreateRoleRequest request
    ) {
        RoleResponse role = roleService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<RoleResponse>builder()
                        .code(HttpStatus.CREATED.value())
                        .message("Tạo role thành công")
                        .data(role)
                        .build());
    }
}
