package com.job_manager.mai.controller.base;

import com.job_manager.mai.contrains.Messages;
import com.job_manager.mai.contrains.Permission;
import com.job_manager.mai.contrains.Roles;
import com.job_manager.mai.model.Account;
import com.job_manager.mai.model.Role;
import com.job_manager.mai.repository.AccountRepository;
import com.job_manager.mai.repository.RoleRepository;
import com.job_manager.mai.util.SecurityHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BaseController {

    private final RoleRepository roleRepository;

    private final AccountRepository accountRepository;

    public boolean checkIfSelf(String username) {
        String currentUsername = SecurityHelper.getLoggedUser();
        Account account = accountRepository.findByUsername(username).orElse(null);
        if (account == null) {
            return false;
        }
        return account.getUsername().equals(currentUsername);
    }

    public void processPermission(Permission permission) throws Exception {

        if (Objects.equals(SecurityHelper.getUserLogged(), String.valueOf(Roles.ANONYMOUS))) {
            throw new RoleNotFoundException(Messages.NO_PERM);
        }
        List<SimpleGrantedAuthority> authorities = SecurityHelper.getLoggedUserAuthorities();
        Role role = roleRepository.findByRoleName(authorities.get(0).getAuthority()).orElseThrow(() -> new RoleNotFoundException(Messages.ROLE_NOT_FOUND));
        if (Objects.equals(role.getRoleName(), String.valueOf(Roles.ROLE_ADMIN))) {
            return;
        }
        for (com.job_manager.mai.model.Permission perm : role.getPermissions()) {
            if (Objects.equals(perm.getName(), String.valueOf(permission))) {
                return;
            }
        }
        throw new RoleNotFoundException(Messages.NO_PERM);
    }
}
