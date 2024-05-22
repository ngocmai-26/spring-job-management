package com.job_manager.mai.service.plan;

import com.job_manager.mai.request.account.DeleteAccountRequest;
import com.job_manager.mai.request.plan.AddPlantRequest;
import com.job_manager.mai.request.plan.DeletePlanRequest;
import com.job_manager.mai.request.plan.PlanRequest;
import com.job_manager.mai.request.plan.UpdatePlanRequest;
import com.job_manager.mai.service.inteface.IBaseService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface PlanService extends IBaseService<PlanRequest, AddPlantRequest, UpdatePlanRequest, DeletePlanRequest, String> {
    ResponseEntity<?> startAPlan(String planId) throws Exception;

    ResponseEntity<?> endAPlan(String planId) throws Exception;

    ResponseEntity<?> getAllPlan(Pageable pageable) throws Exception;
}
