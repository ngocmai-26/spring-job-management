package com.job_manager.mai.schedule;

import com.job_manager.mai.contrains.JobStatus;
import com.job_manager.mai.contrains.NotificationType;
import com.job_manager.mai.model.Job;
import com.job_manager.mai.model.Notification;
import com.job_manager.mai.model.User;
import com.job_manager.mai.model.UserJob;
import com.job_manager.mai.pusher.NotificationPusher;
import com.job_manager.mai.repository.JobRepository;
import com.job_manager.mai.repository.NotificationRepository;
import com.job_manager.mai.repository.UserJobRepository;
import com.job_manager.mai.request.notification.CreateNotificationRequest;
import com.job_manager.mai.service.job.JobService;
import com.job_manager.mai.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@EnableAsync
@Slf4j
@RequiredArgsConstructor
@Component
public class JobChecker {
    private final NotificationRepository notificationRepository;

    private final NotificationPusher notificationPusher;

    private final UserJobRepository userJobRepository;

    private final JobRepository jobRepository;

    @Async
    @Scheduled(cron = "0 0 0 * * ?")
    public void test() {
        //list all job
        List<UserJob> userJobs = userJobRepository.findAllByStatus(JobStatus.PROCESSING);
        for (UserJob userJob : userJobs) {
            Job job = jobRepository.findById(userJob.getJobId()).orElse(null);
            if (job != null) {
                if (job.getJobDetail().getTimeStart().before(new Date(System.currentTimeMillis()))) {
                    // cong viec het han thong bao den cac user nhan cong viec do
                    handlePushNotificationForUser(userJob.getUser(), "Bạn có một công việc đã hết hạn hãy kiểm tra!", job);
                    continue;
                }
                // check cong viec sap het han
                // thoi gian 1 tuan
                long timeOfSevenDay = (3600 * 24 * 7) * 1000;
                if (job.getJobDetail().getTimeEnd().getTime() - new Date(System.currentTimeMillis()).getTime() <= timeOfSevenDay) {
                    // job sap het han
                    handlePushNotificationForUser(userJob.getUser(), "Bạn có một công việc sắp hết hạn hãy kiểm tra!", job);
                    continue;
                }
                handlePushNotificationForUser(userJob.getUser(), "Bạn có một công việc chưa hoàn thành hãy kiểm tra!", job);
            }
        }
        log.info("Job checker planted");
    }

    public void handlePushNotificationForUser(User user, String content, Job job) {
        CreateNotificationRequest createNotificationRequest = createNotificationRequest(job.getManager(), user, content, job.getId());
        notificationPusher.pushNotification(createNotificationRequest);
        Notification notification = new Notification();
        BeanUtils.copyProperties(createNotificationRequest, notification);
        notification.setImage(job.getManager().getAvatar());
        notificationRepository.saveAndFlush(notification);
    }

    private CreateNotificationRequest createNotificationRequest(User from, User to, String content, String jobId) {
        CreateNotificationRequest createNotificationRequest = new CreateNotificationRequest();
        createNotificationRequest.setTo(to);
        createNotificationRequest.setFrom(from);
        createNotificationRequest.setContent(content);
        createNotificationRequest.setType(NotificationType.TYPE_JOB.getValue());
        createNotificationRequest.setDataId(jobId);
        return createNotificationRequest;
    }
}
