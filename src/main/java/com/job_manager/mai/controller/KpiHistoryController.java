package com.job_manager.mai.controller;

import com.job_manager.mai.model.User;
import com.job_manager.mai.repository.UserRepository;
import com.job_manager.mai.service.kpi.KpiHistoryService;
import com.job_manager.mai.util.ApiResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/kpi-histories")
@RequiredArgsConstructor
public class KpiHistoryController {
    private final KpiHistoryService kpiHistoryService;

    private final UserRepository userRepository;

    @GetMapping("/by-user-in-month/{userId}")
    public ResponseEntity<?> getByUserInMonth(@PathVariable(name = "userId") String userId, Pageable pageable) {
        try {
            User user = userRepository.findById(userId).orElse(null);
            return ApiResponseHelper.success(kpiHistoryService.getAllByUser(pageable, user));
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }
}
