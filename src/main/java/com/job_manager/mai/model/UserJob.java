package com.job_manager.mai.model;

import com.job_manager.mai.contrains.JobEvaluate;
import com.job_manager.mai.contrains.JobStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "users_jobs")
@Data
public class UserJob {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private int progress;

    private int cachedProgress;

    @Enumerated(EnumType.STRING)
    private JobStatus status;
    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;
    @ManyToOne
    private User user;
    private String verifyLink;
    private String instructionLink;
    private String denyReason;
    @Enumerated(EnumType.STRING)
    private JobEvaluate jobEvaluate;

    private String jobId;
}
