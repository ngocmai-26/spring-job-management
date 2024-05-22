package com.job_manager.mai.event;

import com.job_manager.mai.request.notification.CreateNotificationRequest;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

public class NotificationEvent extends ApplicationEvent {
    private CreateNotificationRequest request;
    public NotificationEvent(Object source,CreateNotificationRequest request) {
        super(source);
        this.request = request;
    }

    public CreateNotificationRequest getRequest() {
        return request;
    }
}
