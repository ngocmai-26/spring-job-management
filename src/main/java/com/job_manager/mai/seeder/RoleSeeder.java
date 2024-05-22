package com.job_manager.mai.seeder;

import com.job_manager.mai.contrains.Roles;
import com.job_manager.mai.model.Role;
import com.job_manager.mai.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class RoleSeeder implements CommandLineRunner, Ordered {
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        List<String> roles = Arrays.stream(Roles.values()).map(String::valueOf).toList();
        for (String role : roles) {
            try {
                Optional<Role> exitRole = roleRepository.findByRoleName(role);
                if (!exitRole.isPresent()) {
                    Role newRole = new Role();
                    newRole.setRoleName(role);
                    newRole.setCanAction(false);
                    roleRepository.save(newRole);
                }
            } catch (Exception e) {
                continue;
            }
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
