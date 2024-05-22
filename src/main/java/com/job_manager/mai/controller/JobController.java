package com.job_manager.mai.controller;

import com.job_manager.mai.contrains.Permission;
import com.job_manager.mai.controller.base.BaseController;
import com.job_manager.mai.controller.inteface.ICrudController;
import com.job_manager.mai.request.job.*;
import com.job_manager.mai.service.job.JobService;
import com.job_manager.mai.util.ApiResponseHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs")
@CrossOrigin
@Slf4j
public class JobController implements ICrudController<JobRequest, CreateJobRequest, UpdateJobRequest, DeleteJobRequest, String> {
    private final BaseController baseController;

    private final JobService jobService;

    @PostMapping
    @Override
    public ResponseEntity<?> add(@RequestBody CreateJobRequest requestBody) {
        try {
            baseController.processPermission(Permission.MANAGE_JOB_CREATE);
            return jobService.store(requestBody);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping("/by-user/{id}")
    public ResponseEntity<?> getAllJobByUser(@PathVariable(name = "id") String userId, Pageable pageable) {
        try {
            baseController.processPermission(Permission.MANAGE_JOB_CREATE);
            return jobService.getAllByUser(userId, pageable);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<?> update(@RequestBody UpdateJobRequest requestBody, @PathVariable(name = "id") String Id) {
        try {
            baseController.processPermission(Permission.MANAGE_JOB_UPDATE);
            return jobService.edit(requestBody, Id);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable(name = "id") String Id) {
        try {
            baseController.processPermission(Permission.MANAGE_JOB_DELETE);
            return jobService.destroy(Id);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @DeleteMapping
    @Override
    public ResponseEntity<?> deleteAll(@RequestBody DeleteJobRequest request) {
        try {
            baseController.processPermission(Permission.MANAGE_JOB_DELETE);
            return jobService.destroyAll(request);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PutMapping("/verify-progress/{jobId}")
    public ResponseEntity<?> verifyJobProgress(@RequestBody VerifyProgressJob verifyProgressJob, @PathVariable(name = "jobId") String jobId) {
        try {
            baseController.processPermission(Permission.MANAGE_JOB_UPDATE);
            return jobService.verifyJobProgress(jobId, verifyProgressJob);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<?> getById(@PathVariable(name = "id") String s) {
        try {
            baseController.processPermission(Permission.MANAGE_JOB_READ);
            return jobService.getById(s);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping
    @Override
    public ResponseEntity<?> getAll(Pageable pageable) {
        try {
            baseController.processPermission(Permission.MANAGE_JOB_READ);
            return jobService.getAll(pageable);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping("/search")
    @Override
    public ResponseEntity<?> search(@RequestParam(name = "query") String query) {
        try {
//            baseController.processPermission(Permission.MANAGE_JOB_READ);
            return null;
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PutMapping("/job-detail/{id}")
    public ResponseEntity<?> updateJobDetail(@PathVariable(name = "id") String id, @RequestBody JobDetailUpdateRequest request) {
        try {
//            baseController.processPermission(Permission.MANAGE_JOB_UPDATE);
            return jobService.updateJobDetailForJob(id, request);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PutMapping("/user-job/{id}")
    public ResponseEntity<?> updateJobDetailForUser(@PathVariable(name = "id") String id, @RequestBody UpdateUserJobDetailRequest request) {
        try {
            return jobService.updateUserJob(id, request);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PutMapping("/give-for-user/{id}")
    public ResponseEntity<?> giveJobForUser(@PathVariable(name = "id") String id, @RequestBody GiveJobForUserRequest request) {
        try {
//            baseController.processPermission(Permission.MANAGE_JOB_UPDATE);
            return jobService.giveJob(id, request);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }
}
