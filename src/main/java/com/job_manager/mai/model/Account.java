package com.job_manager.mai.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Table(name = "accounts")
@Entity
@Data
public class Account implements UserDetails {
    public Account() {
        this.Id = UUID.randomUUID().toString();
    }

    @Id
    private String Id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false) private String password;

    @Column(name = "is_verify")
    private boolean isVerify = false;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "last_login_time")
    private Date lastLoginTime = new Date(System.currentTimeMillis());

    @Column(name = "last_ip_login")
    private String lastIpLogin;

    @Column(name = "verify_code")
    private String verifyCode;

    @Column(name = "verify_code_expired")
    private Date verifyCodeExpired;

    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;
    @OneToOne
    @Nullable
    private User user;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getRoleName()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isActive;
    }

    @Override
    public boolean isEnabled() {
        return this.isActive && this.isVerify;
    }
}
