package com.job_manager.mai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.job_manager.mai.contrains.PlanStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Plan {

    @Id
    private String id;

    private String title;

    @UpdateTimestamp
    private Date updatedAt;

    @CreationTimestamp
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    private PlanStatus status;

    @ManyToOne
    private User creator;
    @OneToOne
    private PlanDetail planDetail;

    @ManyToMany
    private List<Job> planJobs = new ArrayList<>();

    public void addJob(Job job) {
        this.planJobs.add(job);
    }

    public void removeJob(Job job) {
        this.planJobs.remove(job);
    }

    public void clearJob() {
        this.planJobs.clear();
    }

    public Plan() {
        this.id = UUID.randomUUID().toString();
    }

}
