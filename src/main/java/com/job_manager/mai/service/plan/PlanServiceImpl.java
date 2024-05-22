package com.job_manager.mai.service.plan;

import com.job_manager.mai.contrains.PlanStatus;
import com.job_manager.mai.contrains.PlanType;
import com.job_manager.mai.model.*;
import com.job_manager.mai.repository.*;
import com.job_manager.mai.request.plan.AddPlantRequest;
import com.job_manager.mai.request.plan.DeletePlanRequest;
import com.job_manager.mai.request.plan.UpdatePlanRequest;
import com.job_manager.mai.schedule.PlanScheduleCreator;
import com.job_manager.mai.service.base.BaseService;
import com.job_manager.mai.util.ApiResponseHelper;
import com.job_manager.mai.util.SecurityHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PlanServiceImpl extends BaseService implements PlanService {

    private final PlanScheduleCreator planScheduleCreator;
    private final AccountRepository accountRepository;
    private final JobDetailRepository jobDetailRepository;
    private final PlantRepository plantRepository;

    private final JobRepository jobRepository;
    private final PlanDetailRepository planDetailRepository;

    private final PlanScheduleRepository planScheduleRepository;

    @Override
    public ResponseEntity<?> store(AddPlantRequest request) throws Exception {
        Plan plan = new Plan();
        User creator = SecurityHelper.getUserFromLogged(accountRepository);
        if (creator == null) {
            return ApiResponseHelper.unAuthorized();
        }

        plan.setCreator(creator);
        PlanDetail planDetail = getMapper().map(request.getPlanDetailRequest(), PlanDetail.class);
        planDetail.setPlanType(request.getPlanDetailRequest().getPlanType());
        List<Integer> schedules = request.getPlanDetailRequest().getPlanSchedules();
        for (Integer schedule : schedules) {
            PlanSchedule planSchedule = new PlanSchedule();
            planSchedule.setScheduleType(request.getPlanDetailRequest().getScheduleType());
            planSchedule.setTimeStart(schedule);
            PlanSchedule savedPl = planScheduleRepository.save(planSchedule);
            planDetail.addSchedule(savedPl);
        }

        PlanDetail savedPlanDetail = planDetailRepository.save(planDetail);

        if (request.getPlanJob() != null) {
            request.getPlanJob().forEach((j) -> {
                plan.addJob(jobRepository.findById(j).orElse(null));
            });
        }
        plan.setPlanDetail(savedPlanDetail);
        plan.setTitle(request.getTitle());
        plan.setStatus(PlanStatus.DISABLE);
        return ApiResponseHelper.success(plantRepository.save(plan));
    }

    @Override
    public ResponseEntity<?> edit(UpdatePlanRequest request, String s) throws Exception {
        Plan plan = plantRepository.findById(s).orElseThrow(() -> new UsernameNotFoundException("Plant not found by id " + s));
        BeanUtils.copyProperties(request, plan, getNullPropertyNames(request));
        plan.setStatus(request.getPlanStatus());
        User user = SecurityHelper.getUserFromLogged(accountRepository);
        if (user == null) {
            return ApiResponseHelper.accessDenied();
        }
        if (!user.getId().equals(plan.getCreator().getId())) {
            return ApiResponseHelper.accessDenied();
        }
        if (request.getPlanDetailRequest() != null) {
            PlanDetail planDetail = getMapper().map(request.getPlanDetailRequest(), PlanDetail.class);
            if (request.getPlanDetailRequest().getPlanSchedules() != null) {
                planDetail.clearSchedule();
                for (Integer planSchedule : request.getPlanDetailRequest().getPlanSchedules()) {
                    PlanSchedule schedule = new PlanSchedule();
                    schedule.setScheduleType(request.getPlanDetailRequest().getScheduleType());
                    schedule.setTimeStart(planSchedule);
                    planDetail.addSchedule(planScheduleRepository.save(schedule));
                }
            }
            plan.setPlanDetail(planDetailRepository.save(planDetail));
        }
        if (request.getPlanJob() != null) {
            plan.clearJob();
            for (String s1 : request.getPlanJob()) {
                plan.addJob(jobRepository.findById(s1).orElse(null));
            }
        }
        return ApiResponseHelper.success(plantRepository.save(plan));
    }

    @Override
    public ResponseEntity<?> destroy(String s) throws Exception {
        plantRepository.deleteById(s);
        return ApiResponseHelper.success();
    }

    @Override
    public ResponseEntity<?> destroyAll(DeletePlanRequest request) throws Exception {
        plantRepository.deleteAllById(request.getIds());
        return ApiResponseHelper.success();
    }

    @Override
    public ResponseEntity<?> getAll(Pageable pageable) {
        return null;
    }

    public ResponseEntity<?> getAllPlan(Pageable pageable) throws Exception {
        Page<Plan> planPage = plantRepository.findAll(pageable);

        return ApiResponseHelper.success(planPage);
    }

    @Override
    public ResponseEntity<?> getById(String s) throws Exception {
        return ApiResponseHelper.success(plantRepository.findById(s).orElse(null));
    }

    @Override
    public ResponseEntity<?> searchByName(Pageable pageable, String name) throws Exception {
        return ApiResponseHelper.success(plantRepository.findAllByTitleContaining(pageable, name));
    }

    @Override
    public ResponseEntity<?> SearchAndSortByProperties(Pageable pageable, String searchProperties, String sortProperties) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<?> startAPlan(String planId) throws Exception {
        Plan plan = plantRepository.findById(planId).orElseThrow(() -> new Exception("Plant not found!"));
        if (plan.getPlanDetail().getPlanType().equals(PlanType.LOOP)) {
            PlanHandle handle = new PlanHandle() {
                private JobRepository jobRepository;
                private JobDetailRepository jobDetailRepository;

                private List<Job> oldJobs = new ArrayList<>();

                @Override
                public void inject(JobRepository jobRepository, JobDetailRepository jobDetailRepository) {
                    this.jobDetailRepository = jobDetailRepository;
                    this.jobRepository = jobRepository;
                }

                @Override
                public void setOldJobs(List<Job> jobs) {
                    this.oldJobs = jobs;
                }

                @Override
                public void run() {
                    List<Job> jobs = new ArrayList<>();
                    for (Job job : this.oldJobs) {
                        long timeDiff = job.getJobDetail().getTimeEnd().getTime() - job.getJobDetail().getTimeStart().getTime();
                        Date timeStart = new Date(System.currentTimeMillis());
                        Date timeEnd = new Date(System.currentTimeMillis() + timeDiff);
                        Job newJob = new Job();
                        JobDetail jobDetail = new JobDetail();
                        jobDetail.setTimeStart(timeStart);
                        jobDetail.setTimeEnd(timeEnd);
                        jobDetail.setDescription(job.getJobDetail().getDescription());

                        BeanUtils.copyProperties(job, newJob);
                        newJob.setJobDetail(jobDetailRepository.save(jobDetail));
                        newJob.setManager(job.getManager());
                        newJob.setUserJobs(new ArrayList<>());
                        jobs.add(newJob);
                    }
                    jobRepository.saveAll(jobs);
                }
            };

            handle.inject(jobRepository, jobDetailRepository);
            handle.setOldJobs(plan.getPlanJobs());
            PlanTask planTask = new PlanTask(plan, handle);
            if (planScheduleCreator.checkCanAddByUser(planTask)) {
                planScheduleCreator.createSchedule(planTask);
            } else {
                return ApiResponseHelper.fallback(new Exception("LIMIT OF PLAN TYPE LOOP ACTIVE"));
            }
        }
        plan.setStatus(PlanStatus.ACTIVE);
        plantRepository.save(plan);
        return ApiResponseHelper.success();
    }

    @Override
    public ResponseEntity<?> endAPlan(String planId) throws Exception {
        Plan plan = plantRepository.findById(planId).orElseThrow(() -> new Exception("Plant not found!"));
        plan.setStatus(PlanStatus.DISABLE);
        planScheduleCreator.cancelPlan(planId);
        plantRepository.save(plan);
        return ApiResponseHelper.success();
    }
}
