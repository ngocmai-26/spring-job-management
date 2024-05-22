package com.job_manager.mai.service.kpi;

import com.job_manager.mai.contrains.Roles;
import com.job_manager.mai.model.*;
import com.job_manager.mai.repository.*;
import com.job_manager.mai.request.kpi.CreateKPIRequest;
import com.job_manager.mai.request.kpi.DeleteKPIRequest;
import com.job_manager.mai.request.kpi.UpdateDetailRequest;
import com.job_manager.mai.request.kpi.UpdateKPIRequest;
import com.job_manager.mai.service.base.BaseService;
import com.job_manager.mai.util.ApiResponseHelper;
import com.job_manager.mai.util.SecurityHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Transactional
@RequiredArgsConstructor
@Slf4j
@Service
public class KpiServiceImpl extends BaseService implements KpiService {
    private final KpiRepository kpiRepository;
    private final UserRepository userRepository;
    private final KpiCategoryRepository kpiCategoryRepository;

    private final KpiRepositoryDetail kpiRepositoryDetail;

    private final AccountRepository accountRepository;

    @Override
    public ResponseEntity<?> store(CreateKPIRequest request) throws Exception {
        Kpi kpi = new Kpi();
        kpi.setVerify(false);
        kpi.setName(request.getName());
        kpi.setDescription(request.getDescription());
        kpi.setTarget(request.getTarget());
        KpiCategory kpiCategory = kpiCategoryRepository.findById(request.getKpiTypeId()).orElseThrow(() -> new Exception("Kpi type not found by id : " + request.getKpiTypeId()));
        kpi.setKpiType(kpiCategory);
        KpiDetail kpiDetail = new KpiDetail();
        kpiDetail.setComment(request.getComment());
        kpiDetail.setNote(request.getNote());
        kpiDetail.setTimeEnd(request.getTimeEnd());
        kpiDetail.setTimeStart(request.getTimeStart());
        KpiDetail savedDetail = kpiRepositoryDetail.save(kpiDetail);
        kpi.setDetail(savedDetail);
        User user = SecurityHelper.getUserFromLogged(accountRepository);
        if (user == null) return ApiResponseHelper.unAuthorized();
        kpi.setUser(user);
        return ApiResponseHelper.success(kpiRepository.save(kpi));
    }

    @Override
    public ResponseEntity<?> edit(UpdateKPIRequest request, String s) throws Exception {
        Kpi kpi = kpiRepository.findById(s).orElseThrow(() -> new Exception("Kpi not found by id : " + s));
        User user = SecurityHelper.getUserFromLogged(accountRepository);
        if (user == null) return ApiResponseHelper.unAuthorized();
        if (!Objects.equals(user.getId(), kpi.getUser().getId()))
            return ApiResponseHelper.fallback(new Exception("Kpi not your mine cannot edit"));
        BeanUtils.copyProperties(request, kpi, getNullPropertyNames(request));
        if (request.getKpiTypeId() != null) {
            KpiCategory kpiCategory = kpiCategoryRepository.findById(request.getKpiTypeId()).orElseThrow(() -> new Exception("Kpi type not found by id : " + request.getKpiTypeId()));
            kpi.setKpiType(kpiCategory);
        }
        return ApiResponseHelper.success(kpiRepository.save(kpi));
    }

    @Override
    public ResponseEntity<?> destroy(String s) throws Exception {
        if (beforeDestroy(s)) return ApiResponseHelper.unAuthorized();
        return ApiResponseHelper.success();
    }

    @Override
    public ResponseEntity<?> destroyAll(DeleteKPIRequest request) throws Exception {
        for (String id : request.getIds()) {
            if (beforeDestroy(id)) return ApiResponseHelper.unAuthorized();
        }
        return ApiResponseHelper.success();
    }

    private boolean beforeDestroy(String id) throws Exception {
        User user = SecurityHelper.getUserFromLogged(accountRepository);
        Kpi kpi = kpiRepository.findById(id).orElseThrow(() -> new Exception("Kpi not found by id : " + id));
        if (user == null) return true;
        if (!(user.getId().equals(kpi.getUser().getId()))) {
            Account account = accountRepository.findByUsername(user.getEmail()).orElse(null);
            if (account != null) {
                if (!account.getRole().getRoleName().equals(Roles.ROLE_ADMIN.toString())) {
                    return true;
                }
            }
        }
        kpiRepository.deleteById(id);
        return false;
    }

    @Override
    public ResponseEntity<?> getAll(Pageable pageable) {
        return ApiResponseHelper.success(kpiRepository.findAll(pageable));
    }

    @Override
    public ResponseEntity<?> getById(String s) throws Exception {
        return ApiResponseHelper.success(kpiRepository.findById(s).orElseThrow(() -> new Exception("Kpi not found by id : " + s)));
    }

    @Override
    public ResponseEntity<?> searchByName(Pageable pageable, String name) throws Exception {
        return ApiResponseHelper.success(kpiRepository.findAllByNameContaining(pageable, name));
    }

    @Override
    public ResponseEntity<?> SearchAndSortByProperties(Pageable pageable, String searchProperties, String sortProperties) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<?> updateDetail(UpdateDetailRequest request, String id) throws Exception {
        Kpi kpi = kpiRepository.findById(id).orElseThrow(() -> new Exception("Kpi not found by id : " + id));
        KpiDetail kpiDetail = kpiRepositoryDetail.findById(kpi.getDetail().getId()).orElseThrow(() -> new Exception("Detail not found on kpi with id : " + id));
        BeanUtils.copyProperties(request, kpiDetail, getNullPropertyNames(request));
        KpiDetail saved = kpiRepositoryDetail.save(kpiDetail);
        kpi.setDetail(saved);
        kpiRepository.save(kpi);
        return ApiResponseHelper.success(kpi);
    }

    @Override
    public ResponseEntity<?> verifyKpi(String id) throws Exception {
        Kpi kpi = kpiRepository.findById(id).orElseThrow(() -> new Exception("Kpi not found by id : " + id));
        kpi.setVerify(true);
        // after true reset KPI
        kpi.getUser().setJobPoint(0);
        kpi.getUser().setCheckInPoint(0);
        userRepository.save(kpi.getUser());
        kpiRepository.save(kpi);
        return ApiResponseHelper.success();
    }

    @Override
    public ResponseEntity<?> getKpiByUser(Pageable pageable) throws Exception {
        User user = SecurityHelper.getUserFromLogged(accountRepository);
        return ApiResponseHelper.success(kpiRepository.findAllByUserContaining(pageable, user));
    }
}
