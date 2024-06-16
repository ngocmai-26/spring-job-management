package com.job_manager.mai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Table(name = "users")
@Entity
@Data
public class User {

    public User() {
        this.Id = UUID.randomUUID().toString();
    }

    @Id
    private String Id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(unique = true)
    private String email;
    private String address;
    private Date birthday;

    private String department;

    @ManyToMany
    @JsonIgnore
    private Set<Room> rooms = new HashSet<>();
    @Column(unique = true)
    private String phone;

    private String avatar;

    public String getFullName() {
        return firstName + " " + lastName.trim();
    }

    @OneToMany
    @JoinTable(name = "user_staffs", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "staff_id"))
    private Set<User> staffs = new HashSet<>();

    @ManyToOne
    @JsonIgnore
    private User manager;

//    @ManyToMany(mappedBy = "staffs")
//    @JsonIgnore
//    private Set<Job> userJobs = new HashSet<>();

    @OneToMany(mappedBy = "manager")
    @JsonIgnore
    private Set<Job> userManageJobs = new HashSet<>();

    private float jobPoint = 0;
    private float checkInPoint = 0;

    @OneToMany
    @JoinTable(name = "users_plans", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "plan_id"))
    private Set<Plan> userPlans = new HashSet<>();

    public void addStaff(User staff) {
        this.staffs.add(staff);
    }

    public void removeStaff(User staff) {
        this.staffs.remove(staff);
    }
}
