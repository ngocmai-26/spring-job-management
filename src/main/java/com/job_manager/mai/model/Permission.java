package com.job_manager.mai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.yaml.snakeyaml.events.Event;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    @Column
    private String description;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    Set<Role> roles = new HashSet<>();

    private boolean canDelete;

    private boolean isActive;
}
