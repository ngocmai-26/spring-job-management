package com.job_manager.mai.seeder;

import com.job_manager.mai.contrains.Permission;
import com.job_manager.mai.contrains.Roles;
import com.job_manager.mai.model.Account;
import com.job_manager.mai.model.Role;
import com.job_manager.mai.model.User;
import com.job_manager.mai.repository.AccountRepository;
import com.job_manager.mai.repository.PermissionRepository;
import com.job_manager.mai.repository.RoleRepository;
import com.job_manager.mai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountSeeder implements Ordered, CommandLineRunner {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        try {
            String adminUsername = "admin@gmail.com";
            String adminPassword = "admin";
            Role adminRole = roleRepository.findByRoleName(String.valueOf(Roles.ROLE_ADMIN)).get();
            Optional<Account> exitAccount = accountRepository.findByUsername(adminUsername);
            if (!exitAccount.isPresent()) {
                Account account = new Account();
                account.setPassword(passwordEncoder.encode(adminPassword));
                account.setUsername(adminUsername);
                account.setVerify(true);
                account.setActive(true);
                account.setRole(adminRole);
                account.setLastLoginTime(new Date(System.currentTimeMillis()));
                User user = new User();
                user.setEmail(account.getUsername());
                user.setAddress("Bình Dương");
                user.setBirthday(new Date(System.currentTimeMillis()));
                user.setDepartment("Đại học Bình Dương");
                user.setFirstName("Admin");
                user.setLastName("");
                user.setPhone("");
                account.setUser(
                        userRepository.save(user)
                );
                roleRepository.save(adminRole);
                accountRepository.save(account);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
