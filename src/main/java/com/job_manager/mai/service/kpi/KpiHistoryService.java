package com.job_manager.mai.service.kpi;

import com.job_manager.mai.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface KpiHistoryService {
    public void createNewFromOtherService(String content, User userId);

    public ResponseEntity<?> getAllByUser(Pageable pageable, User userId);
}
