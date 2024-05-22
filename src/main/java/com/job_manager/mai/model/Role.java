package com.job_manager.mai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.*;

@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;

    @Column(name = "description")
    private String description;

    @ManyToMany
    private List<Permission> permissions = new ArrayList<>();

    public void addPerm(Permission permission) {
        this.permissions.add(permission);
    }

    public void removePerm(Permission permission) {
        this.permissions.remove(permission);
        permission.getRoles().remove(this);
    }

    public void clear() {
        this.permissions.clear();
    }

    private boolean canAction;
}
