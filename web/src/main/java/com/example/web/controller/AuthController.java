package com.example.web.controller;

import com.example.web.dto.reponse.ApiResponse;
import com.example.web.dto.reponse.UserResponse;
import com.example.web.dto.request.LoginRequest;
import com.example.web.dto.request.RegisterRequest;
import com.example.web.dto.request.VerifyOtpRequest;
import com.example.web.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/send-otp")
    public ResponseEntity<ApiResponse<Void>> sendRegisterOtp(
            @Valid @RequestBody RegisterRequest request,
            HttpSession session
    ) {
        authService.sendRegisterOtp(request, session);

        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Đã gửi mã OTP đến email")
                .build());
    }

    @PostMapping("/register/verify-otp")
    public ResponseEntity<ApiResponse<UserResponse>> verifyRegisterOtp(
            @Valid @RequestBody VerifyOtpRequest request,
            HttpSession session
    ) {
        UserResponse user = authService.verifyRegisterOtp(request, session);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<UserResponse>builder()
                        .code(HttpStatus.CREATED.value())
                        .message("Đăng ký tài khoản thành công")
                        .data(user)
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(@Valid @RequestBody LoginRequest request, HttpSession session){
        UserResponse user = authService.login(request, session);
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Đăng nhập thành công")
                .data(user)
                .build()
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session){
        authService.logout(session);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Đăng xuất thành công")
                .build());
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> me(HttpSession session){
        UserResponse user = authService.getCurrentUser(session);
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy thông tin thành công")
                .data(user)
                .build()
        );
    }
}
