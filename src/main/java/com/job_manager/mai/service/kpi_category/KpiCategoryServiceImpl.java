package com.job_manager.mai.service.kpi_category;

import com.job_manager.mai.model.KpiCategory;
import com.job_manager.mai.repository.KpiCategoryRepository;
import com.job_manager.mai.request.kpi_category.CreateKpiCategoryRequest;
import com.job_manager.mai.request.kpi_category.UpdateKpiCategoryRequest;
import com.job_manager.mai.util.ApiResponseHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class KpiCategoryServiceImpl implements KpiCategoryService {
    private final KpiCategoryRepository categoryRepository;

    @Override
    public ResponseEntity<?> store(CreateKpiCategoryRequest request) throws Exception {
        KpiCategory category = new KpiCategory();
        category.setName(request.getName());
        return ApiResponseHelper.success(categoryRepository.save(category));
    }

    @Override
    public ResponseEntity<?> edit(UpdateKpiCategoryRequest request, String s) throws Exception {
        KpiCategory category = categoryRepository.findById(s).orElseThrow(() -> new Exception("Category not found by id : " + s));
        category.setName(request.getName());
        return ApiResponseHelper.success(categoryRepository.save(category));
    }

    @Override
    public ResponseEntity<?> destroy(String s) throws Exception {
        categoryRepository.deleteById(s);
        return ApiResponseHelper.success();
    }

    @Override
    public ResponseEntity<?> destroyAll(UpdateKpiCategoryRequest request) throws Exception {
        categoryRepository.deleteAll();
        return ApiResponseHelper.success();
    }

    @Override
    public ResponseEntity<?> getAll(Pageable pageable) {
        return ApiResponseHelper.success(categoryRepository.findAll(pageable));
    }

    @Override
    public ResponseEntity<?> getById(String s) throws Exception {
        return ApiResponseHelper.success(categoryRepository.findById(s).orElseThrow(() -> new Exception("Kpi type not found by id : " + s)));
    }

    @Override
    public ResponseEntity<?> searchByName(Pageable pageable, String name) throws Exception {
        return ApiResponseHelper.success(categoryRepository.findAllByNameContaining(pageable, name));
    }

    @Override
    public ResponseEntity<?> SearchAndSortByProperties(Pageable pageable, String searchProperties, String sortProperties) throws Exception {
        return null;
    }
}
