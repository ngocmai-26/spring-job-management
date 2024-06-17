package com.job_manager.mai.service.job;

import com.job_manager.mai.contrains.*;
import com.job_manager.mai.model.*;
import com.job_manager.mai.model.User;
import com.job_manager.mai.repository.*;
import com.job_manager.mai.request.job.*;
import com.job_manager.mai.request.notification.CreateNotificationRequest;
import com.job_manager.mai.response.job.JobResponse;
import com.job_manager.mai.service.base.BaseService;
import com.job_manager.mai.service.kpi.KpiHistoryService;
import com.job_manager.mai.service.notification.NotificationService;
import com.job_manager.mai.util.ApiResponseHelper;
import com.job_manager.mai.util.SecurityHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class JobServiceImpl extends BaseService implements JobService {

    private final JobRepository jobRepository;
    private final KpiHistoryService kpiHistoryService;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    private final JobDetailRepository jobDetailRepository;
    private final UserJobRepository userJobRepository;

    private final JobResponse jobResponse;

    @Override
    public ResponseEntity<?> store(CreateJobRequest request) throws Exception {
        User user = userRepository.findById(request.getUserCreateJobId()).orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));
        Job job = new Job();
        BeanUtils.copyProperties(request, job, getNullPropertyNames(request));
        if (request.getStaffsGotJobId() != null) {
            for (String sId : request.getStaffsGotJobId()) {
                User staff = userRepository.findById(sId).orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));
                if (!staff.getId().equals(user.getId())) {
                    job.addStaff(staff, userJobRepository);
                }
            }
        }
        job.setManager(user);
        JobDetail jobDetail = new JobDetail();
        jobDetail.setDescription(request.getDescription());
        jobDetail.setNote(request.getNote());
        jobDetail.setTarget(request.getTarget());
        jobDetail.setAdditionInfo(request.getAdditionInfo());
        jobDetail.setTimeStart(request.getTimeStart());
        jobDetail.setTimeEnd(request.getTimeEnd());
        job.setJobDetail(jobDetailRepository.save(jobDetail));
        return pushNotificationForStaff(job, "Bạn vừa được giao một công việc");
    }

    private ResponseEntity<?> pushNotificationForStaff(Job job, String content) throws Exception {
        Job savedJob = jobRepository.save(job);
        for (UserJob staff : savedJob.getUserJobs()) {
            CreateNotificationRequest notification = new CreateNotificationRequest();
            notification.setContent(content);
            notification.setTo(staff.getUser());
            notification.setFrom(job.getManager());
            notification.setType(NotificationType.TYPE_JOB.getValue());
            notification.setDataId(job.getId());
            notificationService.createAndPushNotification(notification);
        }
        return ApiResponseHelper.success(savedJob);
    }

    private void pushNotificationForAStaff(String jobId, User staff, User manager, String content) throws Exception {
        CreateNotificationRequest notification = new CreateNotificationRequest();
        notification.setContent(content);
        notification.setTo(staff);
        notification.setFrom(manager);
        notification.setType(NotificationType.TYPE_JOB.getValue());
        notification.setDataId(jobId);
        notificationService.createAndPushNotification(notification);
    }

    @Override
    public ResponseEntity<?> edit(UpdateJobRequest request, String s) throws Exception {
        Job job = jobRepository.findById(s).orElseThrow(() -> new Exception("Job not found"));
        BeanUtils.copyProperties(request, job, getNullPropertyNames(request));
        return ApiResponseHelper.success(jobRepository.save(job));
    }

    @Override
    public ResponseEntity<?> destroy(String s) throws Exception {
        Job job = jobRepository.findById(s).orElseThrow(() -> new Exception("Job not found"));
        job.setDisplay(false);
        jobRepository.save(job);
        return ApiResponseHelper.success();
    }

    public ResponseEntity<?> verifyJobProgress(String jobId, VerifyProgressJob verifyProgressJob) throws Exception {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new Exception("Job not found by id : " + jobId));
        for (UserJob userJob : job.getUserJobs()) {
            if (userJob.getUser().getId().equals(verifyProgressJob.getUserId())) {
                userJob.setProgress(userJob.getCachedProgress());
                userJobRepository.save(userJob);
            }
        }
        return ApiResponseHelper.success(jobRepository.save(job));
    }

    @Override
    public ResponseEntity<?> evaluateJobForUser(String jobId, EvaluateUserJobRequest request) throws Exception {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new Exception("Job not found"));
        Account currentUser = SecurityHelper.getAccountFromLogged(accountRepository);
        if (currentUser == null) {
            return ApiResponseHelper.accessDenied();
        }
        if (request.getJobEvaluate() != null) {
            if (!Objects.equals(currentUser.getRole().getRoleName(), Roles.ROLE_ADMIN.toString())) {

            }
            // push notification
            job.getUserJobs().forEach((j) -> {
                if (j.getUser().getId().equals(request.getUserId())) {
                    j.setJobEvaluate(request.getJobEvaluate());
                    userJobRepository.save(j);
                    try {
                        CreateNotificationRequest createNotificationRequest = new CreateNotificationRequest();
                        createNotificationRequest.setDataId(jobId);
                        createNotificationRequest.setFrom(job.getManager());
                        createNotificationRequest.setType(NotificationType.TYPE_JOB.getValue());
                        createNotificationRequest.setTo(j.getUser());
                        createNotificationRequest.setContent("Có một công việc đã được đánh giá bởi manager");
                        notificationService.createAndPushNotification(createNotificationRequest);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
            });
            jobRepository.save(job);
        }
        return ApiResponseHelper.success();
    }

    @Override
    public ResponseEntity<?> updateUserJob(String jobId, UpdateUserJobDetailRequest request) throws Exception {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new UsernameNotFoundException("Job not found by id : " + jobId));

        // Duyệt qua từng userJob trong danh sách job.getUserJobs()
        for (UserJob userJob : job.getUserJobs()) {
            if (userJob.getUser().getId().equals(request.getUserId())) {
                int oldProgress = userJob.getProgress();
                BeanUtils.copyProperties(request, userJob);
                userJob.setProgress(oldProgress);
                userJob.setCachedProgress(request.getProgress());

                if (request.getStatus() == JobStatus.DONE) {
                    // Thực hiện các hành động khi request.getStatus() là DONE
                    userJob.setStatus(JobStatus.DONE);
                    userJob.getUser().setJobPoint(userJob.getUser().getJobPoint() + job.getPointPerJob());

                    // Lưu lịch sử cộng điểm
                    KpiHistory kpiHistory = new KpiHistory();
                    kpiHistory.setUser(userJob.getUser());
                    kpiHistoryService.createNewFromOtherService("Cộng " + job.getPointPerJob() + " điểm từ job : " + job.getId(), userJob.getUser());

                    // Gửi thông báo đến nhân viên
                    pushNotificationForAStaff(jobId, userJob.getUser(), job.getManager(), "Bạn vừa hoàn thành một công việc");
                }

                // Lưu userJob vào cơ sở dữ liệu
                userJobRepository.save(userJob);
            }
        }

        // Sau khi duyệt hết danh sách userJobs, lưu lại job vào cơ sở dữ liệu
        jobRepository.save(job);

        // Trả về thành công
        return ApiResponseHelper.success();
    }

    @Override
    public ResponseEntity<?> destroyAll(DeleteJobRequest request) throws Exception {
        request.getIds().forEach((jobId) -> {
            Job job = jobRepository.findById(jobId).orElse(null);
            if (job != null) {
                job.setDisplay(false);
                jobRepository.save(job);
            }
        });
        return ApiResponseHelper.success();
    }

    @Override
    public ResponseEntity<?> getAll(Pageable pageable) {
        return ApiResponseHelper.success(jobRepository.findAllByDisplay(pageable, true));
    }

    @Override
    public ResponseEntity<?> getById(String s) throws Exception {
        Job job = jobRepository.findById(s).orElseThrow(() -> new UsernameNotFoundException("Job not found by id " + s));
        return ApiResponseHelper.success(jobResponse.mapTo(job));
    }

    public ResponseEntity<?> updateJobDetailForJob(String jobId, JobDetailUpdateRequest request) throws Exception {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new Exception("Job not found"));
        Account currentUser = SecurityHelper.getAccountFromLogged(accountRepository);
        if (currentUser == null) {
            return ApiResponseHelper.accessDenied();
        }
        JobDetail jobDetail = job.getJobDetail();

        if (jobDetail == null) {
            jobDetail = new JobDetail();
        }
        BeanUtils.copyProperties(request, jobDetail, getNullPropertyNames(request));
        job.setJobDetail(jobDetailRepository.save(jobDetail));
        jobRepository.save(job);
        return ApiResponseHelper.success();
    }

    @Override
    public ResponseEntity<?> giveJob(String id,
                                     GiveJobForUserRequest request) throws Exception {
        Job job = jobRepository.findById(id).orElseThrow(() -> new Exception("Job not found"));
        if (!SecurityHelper.getLoggedUser().equals(job.getManager().getEmail())) {
            return ApiResponseHelper.fallback(new Exception(""));
        }
        for (String sId : request.getUserIds()) {
            if (!sId.equals(job.getManager().getId())) {
                User user = userRepository.findById(sId).orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));
                boolean hasExit = false;
                for (UserJob userJob : job.getUserJobs()) {
                    if (userJob.getUser().getId().equals(sId)) {
                        hasExit = true;
                    }
                }
                if (!hasExit) {
                    job.addStaff(user, userJobRepository);
                    pushNotificationForAStaff(job.getId(), user, job.getManager(), "Bạn vừa được giao một công việc");
                }
            }
        }
        return ApiResponseHelper.success(jobRepository.save(job));
    }

    @Override
    public ResponseEntity<?> getAllByUser(String userId, Pageable pageable) throws Exception {
        List<UserJob> userJobs = userJobRepository.findAllByUser(userRepository.findById(userId).orElse(null));
        return ApiResponseHelper.success(jobRepository.findAllByUserJobsIn(pageable, userJobs));
    }
}
