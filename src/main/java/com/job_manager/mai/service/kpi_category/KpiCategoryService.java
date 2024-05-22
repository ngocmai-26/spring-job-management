package com.job_manager.mai.service.kpi_category;

import com.job_manager.mai.request.kpi_category.CreateKpiCategoryRequest;
import com.job_manager.mai.request.kpi_category.KpiCategoryRequest;
import com.job_manager.mai.request.kpi_category.UpdateKpiCategoryRequest;
import com.job_manager.mai.service.inteface.IBaseService;

public interface KpiCategoryService extends IBaseService<KpiCategoryRequest, CreateKpiCategoryRequest, UpdateKpiCategoryRequest, UpdateKpiCategoryRequest, String> {
}
