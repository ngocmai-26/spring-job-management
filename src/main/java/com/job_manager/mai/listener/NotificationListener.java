package com.job_manager.mai.listener;

import com.job_manager.mai.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationListener {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @EventListener
    public void pushNotification(NotificationEvent event) {
        simpMessagingTemplate.convertAndSend("/notification", event.getRequest());
    }
}
