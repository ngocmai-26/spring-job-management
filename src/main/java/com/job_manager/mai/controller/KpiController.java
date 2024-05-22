package com.job_manager.mai.controller;

import com.job_manager.mai.controller.inteface.IBaseController;
import com.job_manager.mai.request.kpi.*;
import com.job_manager.mai.service.kpi.KpiService;
import com.job_manager.mai.util.ApiResponseHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kpis")
@RequiredArgsConstructor
@CrossOrigin
public class KpiController implements IBaseController<KPIRequest, CreateKPIRequest, UpdateKPIRequest, DeleteKPIRequest, String> {

    private final KpiService kpiService;

    @PostMapping
    @Override
    public ResponseEntity<?> add(@Valid @RequestBody CreateKPIRequest requestBody) {
        try {
            return kpiService.store(requestBody);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<?> update(@RequestBody UpdateKPIRequest requestBody, @PathVariable(name = "id") String Id) {
        try {
            return kpiService.edit(requestBody, Id);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable(name = "id") String Id) {
        try {
            return kpiService.destroy(Id);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @DeleteMapping
    @Override
    public ResponseEntity<?> deleteAll(@RequestBody DeleteKPIRequest request) {
        try {
            return kpiService.destroyAll(request);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<?> getById(@PathVariable(name = "id") String s) {
        try {
            return kpiService.getById(s);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping
    @Override
    public ResponseEntity<?> getAll(Pageable pageable) {
        try {
            return kpiService.getAll(pageable);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PutMapping("/verify/{id}")
    public ResponseEntity<?> verifyKpi(@PathVariable(name = "id") String id) {
        try {
            return kpiService.verifyKpi(id);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping("/by-user")
    public ResponseEntity<?> getAllByUser(Pageable pageable) {
        try {
            return kpiService.getKpiByUser(pageable);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PutMapping("/update-detail/{id}")
    public ResponseEntity<?> updateDetail(@RequestBody UpdateDetailRequest request, @PathVariable(name = "id") String id) {
        try {
            return kpiService.updateDetail(request, id);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @Override
    public ResponseEntity<?> search(String query) {
        return null;
    }

    @Override
    public ResponseEntity<?> searchByName(Pageable pageable, String name) {
        return null;
    }

    @Override
    public ResponseEntity<?> sortByName(String name) {
        return null;
    }
}
