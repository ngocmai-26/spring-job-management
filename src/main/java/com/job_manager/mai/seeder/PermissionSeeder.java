package com.job_manager.mai.seeder;

import com.job_manager.mai.contrains.Permission;
import com.job_manager.mai.contrains.Roles;
import com.job_manager.mai.model.Role;
import com.job_manager.mai.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PermissionSeeder implements CommandLineRunner, Ordered {

    private final PermissionRepository permissionRepository;

    @Override
    public void run(String... args) throws Exception {
        List<String> permission = Arrays.stream(Permission.values()).map(String::valueOf).toList();
        for (String perm : permission) {
            try {
                Optional<com.job_manager.mai.model.Permission> exitRole = permissionRepository.findByName(perm);
                if (!exitRole.isPresent()) {
                    com.job_manager.mai.model.Permission newPerm = new com.job_manager.mai.model.Permission();
                    newPerm.setName(perm);
                    newPerm.setDescription("");
                    newPerm.setCanDelete(false);
                    newPerm.setActive(true);
                    permissionRepository.saveAndFlush(newPerm);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
