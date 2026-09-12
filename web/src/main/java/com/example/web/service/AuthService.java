package com.example.web.service;

import com.example.web.dto.reponse.UserResponse;
import com.example.web.dto.request.LoginRequest;
import com.example.web.dto.request.RegisterRequest;
import com.example.web.dto.request.VerifyOtpRequest;
import jakarta.servlet.http.HttpSession;

public interface AuthService {
    void sendRegisterOtp(RegisterRequest request, HttpSession session);

    UserResponse verifyRegisterOtp(VerifyOtpRequest request, HttpSession session);
    UserResponse login(LoginRequest request, HttpSession session);
    void logout(HttpSession session);
    UserResponse getCurrentUser(HttpSession session);
}
