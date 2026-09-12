package com.example.web.dto.session;

import com.example.web.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class PendingRegistration implements Serializable {
    private final String fullName;
    private final Gender gender;
    private final LocalDate dateOfBirth;
    private final String email;
    private final String phone;
    private final String passwordHash;
}
