package com.job_manager.mai.listener;

import com.job_manager.mai.contrains.ContactEvent;
import com.job_manager.mai.contrains.ContactStatus;
import com.job_manager.mai.contrains.NotificationType;
import com.job_manager.mai.event.NewContactRequestEvent;
import com.job_manager.mai.event.ResponseContactRequestEvent;
import com.job_manager.mai.model.Contact;
import com.job_manager.mai.request.notification.CreateNotificationRequest;
import com.job_manager.mai.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContactListener {

    @AllArgsConstructor
    @Data
    public static class ContactEventResponse {
        private Contact contact;
        private int type;
    }

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationService notificationService;

    @EventListener
    public void responseContactRequestEvent(ResponseContactRequestEvent event) {
        convertAndSendEvent(new ContactEventResponse(event.getContact(), ContactEvent.RESPONSE_REQUEST.getValue()));
    }

    public void convertAndSendEvent(ContactEventResponse contactEventResponse) {
        try {
            CreateNotificationRequest createNotificationRequest = new CreateNotificationRequest();
            createNotificationRequest.setTo(contactEventResponse.getContact().getRelate());
            createNotificationRequest.setFrom(contactEventResponse.getContact().getOwner());
            createNotificationRequest.setContent(contactEventResponse.getType() == ContactEvent.NEW_REQUEST.getValue() ? "Bạn vừa nhận được một lời mời kết bạn" : contactEventResponse.getContact().getStatus() == ContactStatus.BE_FRIEND ? String.format("%s đã chấp nhận lời mời kết bạn", contactEventResponse.getContact().getRelate().getFullName()) : String.format("%s đã từ chối lời mời kết bạn", contactEventResponse.getContact().getRelate().getFullName()));
            createNotificationRequest.setType(NotificationType.TYPE_CONTACT.getValue());
            createNotificationRequest.setDataId(String.valueOf(contactEventResponse.getContact().getId()));
            notificationService.createAndPushNotification(createNotificationRequest);
            simpMessagingTemplate.convertAndSend("/contact", contactEventResponse);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @EventListener
    public void newContactRequestEvent(NewContactRequestEvent event) {
        convertAndSendEvent(new ContactEventResponse(event.getContact(), ContactEvent.NEW_REQUEST.getValue()));
    }
}
