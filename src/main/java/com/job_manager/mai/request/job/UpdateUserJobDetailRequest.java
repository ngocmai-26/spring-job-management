package com.job_manager.mai.request.job;

import com.job_manager.mai.contrains.JobStatus;
import lombok.Data;

@Data
public class UpdateUserJobDetailRequest {
    private String userId;
    private int progress;
    private String verifyLink;
    private String instructionLink;
    private String denyReason;
    private JobStatus status;
}
