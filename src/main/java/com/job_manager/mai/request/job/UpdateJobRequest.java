package com.job_manager.mai.request.job;

import com.job_manager.mai.contrains.JobStatus;
import lombok.Data;

@Data
public class UpdateJobRequest extends JobRequest {
    private String title;
    private int priority;
    private int pointPerJob;

    private boolean isTask;
}
