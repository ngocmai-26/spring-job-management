package com.job_manager.mai.model;

import com.job_manager.mai.repository.JobDetailRepository;
import com.job_manager.mai.repository.JobRepository;

import java.util.ArrayList;
import java.util.List;

public interface PlanHandle extends Runnable {
    List<Job> oldJobs = new ArrayList<>();

    public void inject(JobRepository jobRepository, JobDetailRepository jobDetailRepository);

    public void setOldJobs(List<Job> jobs);
}
