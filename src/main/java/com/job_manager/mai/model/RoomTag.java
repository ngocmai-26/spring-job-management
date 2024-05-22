package com.job_manager.mai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class RoomTag {

    public RoomTag() {
    }

    public RoomTag(String name) {
        this.name = name;
    }

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;
    private String name;
    @ManyToMany
    @JsonIgnore
    private Set<Room> rooms = new HashSet<>();
}
