package com.job_manager.mai.service.kpi;

import com.job_manager.mai.model.KpiHistory;
import com.job_manager.mai.model.User;
import com.job_manager.mai.repository.KpiHistoryRepository;
import com.job_manager.mai.util.ApiResponseHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class KpiHistoryServiceImpl implements KpiHistoryService {

    private final KpiHistoryRepository kpiHistoryRepository;

    @Override
    public void createNewFromOtherService(String content, User user) {
        KpiHistory kpiHistory = new KpiHistory();
        kpiHistory.setUser(user);
        kpiHistory.setContent(content);
        kpiHistoryRepository.save(kpiHistory);
    }

    @Override
    public ResponseEntity<?> getAllByUser(Pageable pageable, User user) {
        LocalDate today = LocalDate.now();

        LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());

        LocalDate lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        Date startDate = Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return ApiResponseHelper.success(
                kpiHistoryRepository.findAllByUserAndCreatedAtIsBetween(pageable, user, startDate, endDate)
        );
    }
}
