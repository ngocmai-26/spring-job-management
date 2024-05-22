package com.job_manager.mai.schedule;

import com.job_manager.mai.contrains.NotificationType;
import com.job_manager.mai.contrains.PlanStatus;
import com.job_manager.mai.model.Plan;
import com.job_manager.mai.repository.PlantRepository;
import com.job_manager.mai.request.notification.CreateNotificationRequest;
import com.job_manager.mai.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PlanChecker {

    private final PlantRepository plantRepository;

    private final NotificationService notificationService;

    @Async
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkPlanTask() {
        List<Plan> plans = plantRepository.findAllByStatus(PlanStatus.ACTIVE);
        for (Plan plan : plans) {
            Date now = new Date(System.currentTimeMillis());
            long timeOf7Days = (3600) * 24 * 7;
            if (plan.getPlanDetail().getTimeEnd().before(now)) {
                pushNotification(plan, "Bạn có một kế hoạch bị trễ hạn, hãy kiểm tra ngay");
                continue;
            }
            if (plan.getPlanDetail().getTimeEnd().getTime() - now.getTime() <= timeOf7Days) {
                pushNotification(plan, "Bạn có một kế hoạch sắp hết hạn, hãy kiểm tra ngay");
            }
        }
        log.info("Plan checker planted");
    }

    public void pushNotification(Plan plan, String content) {
        CreateNotificationRequest createNotificationRequest = new CreateNotificationRequest();
        createNotificationRequest.setContent(content);
        createNotificationRequest.setType(NotificationType.TYPE_PLAN.getValue());
        createNotificationRequest.setFrom(null);
        createNotificationRequest.setTo(plan.getCreator());
        createNotificationRequest.setDataId(plan.getId());
    }
}
