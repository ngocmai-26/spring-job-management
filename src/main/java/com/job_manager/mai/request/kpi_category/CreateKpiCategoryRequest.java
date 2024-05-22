package com.job_manager.mai.request.kpi_category;

import lombok.Data;

@Data
public class CreateKpiCategoryRequest extends KpiCategoryRequest {
    private String name;
}
