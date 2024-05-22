package com.job_manager.mai.service.job;

import com.job_manager.mai.request.job.*;
import com.job_manager.mai.service.inteface.ICrudService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface JobService extends ICrudService<JobRequest, CreateJobRequest, UpdateJobRequest, DeleteJobRequest, String> {
    ResponseEntity<?> updateJobDetailForJob(String jobId, JobDetailUpdateRequest request) throws Exception;

    ResponseEntity<?> giveJob(String id, GiveJobForUserRequest request) throws Exception;

    ResponseEntity<?> getAllByUser(String userId, Pageable pageable) throws Exception;

    ResponseEntity<?> verifyJobProgress(String jobId, VerifyProgressJob verifyProgressJob) throws Exception;

    ResponseEntity<?> evaluateJobForUser(String jobId, EvaluateUserJobRequest request) throws Exception;

    ResponseEntity<?> updateUserJob(String jobId, UpdateUserJobDetailRequest request) throws Exception;
}
