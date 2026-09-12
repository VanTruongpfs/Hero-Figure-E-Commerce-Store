package com.example.web.service.implement;

import com.example.web.dto.reponse.RoleResponse;
import com.example.web.dto.request.CreateRoleRequest;
import com.example.web.exception.BadRequestException;
import com.example.web.model.Role;
import com.example.web.repository.RoleRepository;
import com.example.web.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class RoleServiceImplement implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public RoleResponse create(CreateRoleRequest request) {
        String roleName = normalizeRoleName(request.getName());

        if (roleRepository.existsByName(roleName)) {
            throw new BadRequestException("Role " + roleName + " đã tồn tại");
        }

        Role savedRole = roleRepository.save(Role.builder()
                .name(roleName)
                .build());

        return RoleResponse.builder()
                .id(savedRole.getId())
                .name(savedRole.getName())
                .build();
    }

    private String normalizeRoleName(String name) {
        String normalized = name.trim().toUpperCase(Locale.ROOT);
        return normalized.startsWith("ROLE_") ? normalized : "ROLE_" + normalized;
    }
}
