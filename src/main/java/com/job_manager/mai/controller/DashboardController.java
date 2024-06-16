package com.job_manager.mai.controller;

import com.job_manager.mai.contrains.JobStatus;
import com.job_manager.mai.contrains.PlanStatus;
import com.job_manager.mai.contrains.Roles;
import com.job_manager.mai.model.Role;
import com.job_manager.mai.model.User;
import com.job_manager.mai.repository.*;
import com.job_manager.mai.util.ApiResponseHelper;
import com.job_manager.mai.util.SecurityHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.temporal.TemporalAdjusters;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin
@RequiredArgsConstructor
public class DashboardController {

    private final AccountRepository accountRepository;

    private final PlantRepository plantRepository;
    private final UserJobRepository userJobRepository;
    private final JobRepository jobRepository;
    private final  KpiRepository kpiRepository;
    private  final  UserRepository userRepository;


    private final  CheckInRepository checkInRepository;

    @GetMapping
    ResponseEntity<?> getData() {
        Role role = SecurityHelper.getAccountFromLogged(accountRepository).getRole();
        User user = SecurityHelper.getUserFromLogged(accountRepository);
        Map<String, Object> data = new HashMap<>();
        LocalDateTime timeStart = LocalDateTime.now().withDayOfMonth(1);
        LocalDateTime timeEnd = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth());

        data.put("runningPlan", plantRepository.countAllByStatus(PlanStatus.ACTIVE));
        data.put("stopPlan", plantRepository.countAllByStatus(PlanStatus.DISABLE));
        if (role.getRoleName().equals(Roles.ROLE_STAFF.toString())) {
            data.put("myJob", userJobRepository.countAllByUserAndStatus(user, JobStatus.PROCESSING));
            data.put("checkedIn", checkInRepository.countAllByUserCheckedAndCheckInTimeBetween(user, timeStart, timeEnd));
            Map<String, Object> dataJobInMonth = new HashMap<>();
            dataJobInMonth.put("jobDone", userJobRepository.countAllByUserAndStatusAndUpdatedAtBetween(user, JobStatus.DONE, Date.from(timeStart.atZone(ZoneId.systemDefault()).toInstant()), Date.from(timeEnd.atZone(ZoneId.systemDefault()).toInstant())));
            data.put("dataJobInMonth", dataJobInMonth);
            Map<String, Object> jobStatusInMonth = new HashMap<>();
            jobStatusInMonth.put("jobsInMonth", userJobRepository.findAllByUserAndUpdatedAtBetween(user, Date.from(timeStart.atZone(ZoneId.systemDefault()).toInstant()), Date.from(timeEnd.atZone(ZoneId.systemDefault()).toInstant())));
            data.put("statusJobInMonth", jobStatusInMonth);
            Map<String, Object> dataKpiInMonth = new HashMap<>();
            dataKpiInMonth.put("Kpi", kpiRepository.findAllByUserAndUpdatedAtBetween(user, Date.from(timeStart.atZone(ZoneId.systemDefault()).toInstant()), Date.from(timeEnd.atZone(ZoneId.systemDefault()).toInstant())));
            data.put("statusKPI", dataKpiInMonth);
        }
        if (role.getRoleName().equals(Roles.ROLE_MANAGER.toString())) {
            data.put("giveJob", jobRepository.countAllByManager(user));
            data.put("myJob", userJobRepository.countAllByUserAndStatus(user, JobStatus.PROCESSING));
            Map<String, Object> jobStatusInMonth = new HashMap<>();
            jobStatusInMonth.put("jobsInMonth", jobRepository.findAllByManagerAndUpdatedAtBetween(user, Date.from(timeStart.atZone(ZoneId.systemDefault()).toInstant()), Date.from(timeEnd.atZone(ZoneId.systemDefault()).toInstant())));
            data.put("statusJobInMonth", jobStatusInMonth);
        }

        return ApiResponseHelper.success(data);
    }

}
