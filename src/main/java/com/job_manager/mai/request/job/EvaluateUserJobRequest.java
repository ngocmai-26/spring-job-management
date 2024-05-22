package com.job_manager.mai.request.job;

import com.job_manager.mai.contrains.JobEvaluate;
import lombok.Data;

@Data
public class EvaluateUserJobRequest {
    private JobEvaluate jobEvaluate;
    private String userId;
}
