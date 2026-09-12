package com.example.web.security;

import com.example.web.model.Role;
import com.example.web.model.User;
import com.example.web.repository.RoleRepository;
import com.example.web.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication
    )throws IOException, ServletException {
        OAuth2User googleUser = (OAuth2User) authentication.getPrincipal();
        String email = googleUser.getAttribute("email");
        String fullname = googleUser.getAttribute("name");
        String avatarUrl = googleUser.getAttribute("picture");
        Boolean emailVerified = googleUser.getAttribute("email_verified");

        if(email == null || !Boolean.TRUE.equals(emailVerified)){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Email chưa được xác thực");
            return;
        }
        User user = userRepository.findByEmail(email.toLowerCase().trim()).orElse(null);
        if(user == null){
            Role role = roleRepository.findByName("ROLE_CUSTOMER")
                    .orElseThrow(() ->
                            new RuntimeException("Không tìm thấy role ROLE_CUSTOMER")
                    );
            user = User.builder()
                    .email(email)
                    .fullName(fullname == null || fullname.isBlank() ? email : fullname)
                    .avatarUrl(avatarUrl)
                    .passwordHash(null)
                    .role(role)
                    .status(true)
                    .build();
            userRepository.save(user);
        }
        if (!user.isStatus()) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Tài khoản đã bị khóa"
            );
            return;
        }
        HttpSession session = request.getSession(true);
        session.setAttribute("LOGIN_USER_ID", user.getId());
        response.sendRedirect("/api/v1/auth/me");

    }

}
