package com.job_manager.mai.controller;

import com.job_manager.mai.controller.inteface.IBaseController;
import com.job_manager.mai.request.plan.AddPlantRequest;
import com.job_manager.mai.request.plan.DeletePlanRequest;
import com.job_manager.mai.request.plan.PlanRequest;
import com.job_manager.mai.request.plan.UpdatePlanRequest;
import com.job_manager.mai.service.plan.PlanService;
import com.job_manager.mai.util.ApiResponseHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
@CrossOrigin

public class PlantController implements IBaseController<PlanRequest, AddPlantRequest, UpdatePlanRequest, DeletePlanRequest, String> {
    private final PlanService planService;

    @PostMapping
    @Override
    public ResponseEntity<?> add(@Valid @RequestBody AddPlantRequest requestBody) {
        try {
            return planService.store(requestBody);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<?> update(@RequestBody UpdatePlanRequest requestBody, @PathVariable(name = "id") String Id) {
        try {
            return planService.edit(requestBody, Id);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> startPlan(@PathVariable(name = "id") String planId) {
        try {
            return planService.startAPlan(planId);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable(name = "id") String Id) {
        try {
            return planService.destroy(Id);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @DeleteMapping
    @Override
    public ResponseEntity<?> deleteAll(@RequestBody DeletePlanRequest request) {
        try {
            return planService.destroyAll(request);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<?> getById(@PathVariable(name = "id") String s) {
        try {
            return planService.getById(s);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping
    @Override
    public ResponseEntity<?> getAll(Pageable pageable) {
        try {
            return planService.getAllPlan(pageable);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @Override
    public ResponseEntity<?> search(String query) {
        return null;
    }

    @GetMapping("/by-name")
    @Override
    public ResponseEntity<?> searchByName(Pageable pageable, String name) {
        try {
            return planService.searchByName(pageable, name);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @Override
    public ResponseEntity<?> sortByName(String name) {
        return null;
    }
}
