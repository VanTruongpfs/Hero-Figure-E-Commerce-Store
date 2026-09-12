package com.example.web.service.implement;

import com.example.web.dto.reponse.UserResponse;
import com.example.web.dto.request.LoginRequest;
import com.example.web.dto.request.RegisterRequest;
import com.example.web.dto.request.VerifyOtpRequest;
import com.example.web.dto.session.PendingRegistration;
import com.example.web.exception.BadRequestException;
import com.example.web.mapper.UserMapper;
import com.example.web.model.Role;
import com.example.web.model.User;
import com.example.web.repository.RoleRepository;
import com.example.web.repository.UserRepository;
import com.example.web.service.AuthService;
import com.example.web.service.MailService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService {

    private static final String PENDING_REGISTRATION = "PENDING_REGISTRATION";
    private static final String REGISTER_OTP = "REGISTER_OTP";
    private static final String OTP_EXPIRES_AT = "OTP_EXPIRES_AT";
    private static final String OTP_EMAIL_CONTENT = """
            Mã xác minh tài khoản của bạn là: %s
            Mã có hiệu lực trong 5 phút.
            """;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final UserMapper userMapper;

    private static final String LOGIN_USER_ID = "LOGIN_USER_ID";

    @Override
    public void sendRegisterOtp(RegisterRequest request, HttpSession session) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Mật khẩu xác nhận không khớp");
        }

        String email = normalizeEmail(request.getEmail());
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email này đã được đăng ký");
        }

        PendingRegistration pendingRegistration = new PendingRegistration(
                request.getFullName().trim(),
                request.getGender(),
                request.getDateOfBirth(),
                email,
                request.getPhone(),
                passwordEncoder.encode(request.getPassword())
        );
        String otp = String.format("%06d", new SecureRandom().nextInt(1_000_000));

        session.setAttribute(PENDING_REGISTRATION, pendingRegistration);
        session.setAttribute(REGISTER_OTP, otp);
        session.setAttribute(OTP_EXPIRES_AT, LocalDateTime.now().plusMinutes(5));

        mailService.sendMail(
                email,
                "Mã xác minh đăng ký tài khoản",
                OTP_EMAIL_CONTENT,
                otp
        );
    }

    @Override
    @Transactional
    public UserResponse verifyRegisterOtp(VerifyOtpRequest request, HttpSession session) {
        PendingRegistration pendingRegistration = (PendingRegistration) session.getAttribute(PENDING_REGISTRATION);
        String savedOtp = (String) session.getAttribute(REGISTER_OTP);
        LocalDateTime expiresAt = (LocalDateTime) session.getAttribute(OTP_EXPIRES_AT);

        if (pendingRegistration == null || savedOtp == null || expiresAt == null) {
            throw new BadRequestException("Không tìm thấy yêu cầu đăng ký hoặc mã OTP đã được sử dụng");
        }

        if (!pendingRegistration.getEmail().equals(normalizeEmail(request.getEmail()))) {
            throw new BadRequestException("Email không khớp với yêu cầu đăng ký");
        }

        if (LocalDateTime.now().isAfter(expiresAt)) {
            clearRegistrationSession(session);
            throw new BadRequestException("Mã OTP đã hết hạn");
        }

        if (!savedOtp.equals(request.getOtp())) {
            throw new BadRequestException("Mã OTP không đúng");
        }

        if (userRepository.existsByEmail(pendingRegistration.getEmail())) {
            clearRegistrationSession(session);
            throw new BadRequestException("Email này đã được đăng ký");
        }

        Role userRole = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy role ROLE_CUSTOMER"));

        User user = User.builder()
                .fullName(pendingRegistration.getFullName())
                .gender(pendingRegistration.getGender())
                .dateOfBirth(pendingRegistration.getDateOfBirth())
                .email(pendingRegistration.getEmail())
                .phone(pendingRegistration.getPhone())
                .passwordHash(pendingRegistration.getPasswordHash())
                .avatarUrl("")
                .role(userRole)
                .status(true)
                .build();

        User savedUser = userRepository.save(user);
        clearRegistrationSession(session);

        return userMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse login(LoginRequest request, HttpSession session) {
        String email = normalizeEmail(request.getEmail());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Email hoặc mật khẩu chính xác")
                );
        if(user.getPasswordHash()==null || !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Email hoặc mật khẩu không chính xác");
        }
        if(!user.isStatus()){
            throw new BadRequestException("Tài khoản đã bị khóa");
        }
        session.setAttribute(LOGIN_USER_ID, user.getId());

        return userMapper.toResponse(user);
    }

    @Override
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @Override
    public UserResponse getCurrentUser(HttpSession session) {
        Long userID = (Long) session.getAttribute(LOGIN_USER_ID);
        if(userID == null) {
            throw new BadRequestException("Bạn chưa đăng nhập");
        }
        User user = userRepository.findById(userID).orElseThrow(()->
                 new BadRequestException("Không tìm thấy người dùng")
        );
        return userMapper.toResponse(user);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private void clearRegistrationSession(HttpSession session) {
        session.removeAttribute(PENDING_REGISTRATION);
        session.removeAttribute(REGISTER_OTP);
        session.removeAttribute(OTP_EXPIRES_AT);
    }

}
