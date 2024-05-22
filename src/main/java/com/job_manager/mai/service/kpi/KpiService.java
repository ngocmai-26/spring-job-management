package com.job_manager.mai.service.kpi;

import com.job_manager.mai.request.kpi.*;
import com.job_manager.mai.service.inteface.IBaseService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface KpiService extends IBaseService<KPIRequest, CreateKPIRequest, UpdateKPIRequest, DeleteKPIRequest, String> {
    ResponseEntity<?> updateDetail(UpdateDetailRequest request, String id) throws Exception;

    ResponseEntity<?> verifyKpi(String id) throws Exception;

    ResponseEntity<?> getKpiByUser(Pageable pageable) throws Exception;
}
