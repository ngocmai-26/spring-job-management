package com.job_manager.mai.pusher;

import com.job_manager.mai.event.NotificationEvent;
import com.job_manager.mai.request.notification.CreateNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotificationPusher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void pushNotification(CreateNotificationRequest notificationRequest) {
        applicationEventPublisher.publishEvent(new NotificationEvent(this, notificationRequest));
    }
}
