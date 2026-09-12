package com.example.web.config;

import com.example.web.model.Role;
import com.example.web.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        initRole("ROLE_USER");
        initRole("ROLE_CUSTOMER");
        initRole("ROLE_ADMIN");
    }

    private void initRole(String roleName) {
        if (!roleRepository.existsByName(roleName)) {
            Role role = Role.builder()
                    .name(roleName)
                    .build();
            roleRepository.save(role);
            log.info("Initialized default role: {}", roleName);
        }
    }
}
