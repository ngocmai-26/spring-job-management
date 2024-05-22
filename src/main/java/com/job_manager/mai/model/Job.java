package com.job_manager.mai.model;

import com.job_manager.mai.contrains.JobEvaluate;
import com.job_manager.mai.contrains.JobStatus;
import com.job_manager.mai.repository.UserJobRepository;
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
public class Job {
    @Id
    private String id;
    private String title;

    private int priority;

    @ManyToMany
    private List<UserJob> userJobs = new ArrayList<>();

    @ManyToOne
    private User manager;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    private int pointPerJob;

    private boolean display = true;

    @OneToOne
    private JobDetail jobDetail;

    public Job() {
        this.id = UUID.randomUUID().toString();
    }

    public void addStaff(User user, UserJobRepository userJobRepository) {
        UserJob userJob = new UserJob();
        userJob.setProgress(0);
        userJob.setCachedProgress(0);
        userJob.setStatus(JobStatus.PROCESSING);
        userJob.setUser(user);
        userJob.setJobId(this.id);
        this.userJobs.add(userJobRepository.save(userJob));
    }
}
