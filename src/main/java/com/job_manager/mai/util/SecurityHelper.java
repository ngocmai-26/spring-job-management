package com.job_manager.mai.util;

import com.job_manager.mai.model.Account;
import com.job_manager.mai.model.User;
import com.job_manager.mai.repository.AccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

public class SecurityHelper {

    public static User getUserFromLogged(AccountRepository accountRepository) {
        String username = SecurityHelper.getLoggedUser();
        if (username == null) return null;
        Account account = accountRepository.findByUsername(username).orElse(null);
        if (account == null) return null;
        return account.getUser();
    }

    public static Account getAccountFromLogged(AccountRepository accountRepository) {
        String username = SecurityHelper.getLoggedUser();
        if (username == null) return null;
        Account account = accountRepository.findByUsername(username).orElse(null);
        if (account == null) return null;
        return account;
    }

    public static boolean isLogged() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }

    public static String getLoggedUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public static UserDetails getUserLogged() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static List<SimpleGrantedAuthority> getLoggedUserAuthorities() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .map((gr) -> new SimpleGrantedAuthority(gr.getAuthority()))
                .collect(Collectors.toList());
    }
}
