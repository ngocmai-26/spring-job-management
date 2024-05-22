package com.job_manager.mai.controller;

import com.job_manager.mai.controller.inteface.IBaseController;
import com.job_manager.mai.request.kpi_category.CreateKpiCategoryRequest;
import com.job_manager.mai.request.kpi_category.KpiCategoryRequest;
import com.job_manager.mai.request.kpi_category.UpdateKpiCategoryRequest;
import com.job_manager.mai.service.kpi_category.KpiCategoryService;
import com.job_manager.mai.util.ApiResponseHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kpi-categories")
@CrossOrigin
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
@RequiredArgsConstructor
public class KpiCategoryController implements IBaseController<KpiCategoryRequest, CreateKpiCategoryRequest, UpdateKpiCategoryRequest, UpdateKpiCategoryRequest, String> {
    private final KpiCategoryService kpiCategoryService;

    @PostMapping
    @Override
    public ResponseEntity<?> add(@Valid @RequestBody CreateKpiCategoryRequest requestBody) {
        try {
            return kpiCategoryService.store(requestBody);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<?> update(@RequestBody UpdateKpiCategoryRequest requestBody, @PathVariable(name = "id") String Id) {
        try {
            return kpiCategoryService.edit(requestBody, Id);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable(name = "id") String Id) {
        try {
            return kpiCategoryService.destroy(Id);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @DeleteMapping
    @Override
    public ResponseEntity<?> deleteAll(UpdateKpiCategoryRequest request) {
        try {
            return kpiCategoryService.destroyAll(request);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<?> getById(@PathVariable(name = "id") String s) {
        try {
            return kpiCategoryService.getById(s);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping
    @Override
    public ResponseEntity<?> getAll(Pageable pageable) {
        try {
            return kpiCategoryService.getAll(pageable);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @Override
    public ResponseEntity<?> search(String query) {
        return null;
    }

    @GetMapping("/search-by-name")
    @Override
    public ResponseEntity<?> searchByName(Pageable pageable, @RequestParam(name = "name") String name) {
        try {
            return kpiCategoryService.searchByName(pageable, name);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @Override
    public ResponseEntity<?> sortByName(String name) {
        return null;
    }
}
