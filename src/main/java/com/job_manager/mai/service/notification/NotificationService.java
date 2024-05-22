package com.job_manager.mai.service.notification;

import com.job_manager.mai.request.notification.CreateNotificationRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface NotificationService {
    public void createAndPushNotification(CreateNotificationRequest request) throws Exception;

    public ResponseEntity<?> getAllNotificationByUser(Pageable pageable) throws Exception;

    ResponseEntity<?> read(Long id) throws Exception;
}
