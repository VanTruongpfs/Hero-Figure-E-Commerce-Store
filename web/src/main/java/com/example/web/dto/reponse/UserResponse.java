package com.example.web.dto.reponse;

import com.example.web.model.Gender;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String fullName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String email;
    private String phone;
    private String avatarUrl;
    private Long roleId;
    private String roleName;
    private boolean status;
    private LocalDateTime createdAt;
}
