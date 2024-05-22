package com.job_manager.mai.service.notification;

import com.job_manager.mai.model.Notification;
import com.job_manager.mai.model.User;
import com.job_manager.mai.pusher.NotificationPusher;
import com.job_manager.mai.repository.AccountRepository;
import com.job_manager.mai.repository.NotificationRepository;
import com.job_manager.mai.request.notification.CreateNotificationRequest;
import com.job_manager.mai.service.base.BaseService;
import com.job_manager.mai.util.ApiResponseHelper;
import com.job_manager.mai.util.SecurityHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class NotificationServiceIpm extends BaseService implements NotificationService {

    private final NotificationPusher notificationPusher;
    private final NotificationRepository notificationRepository;

    private final AccountRepository accountRepository;

    @Override
    public void createAndPushNotification(CreateNotificationRequest request) throws Exception {
        Notification notification = new Notification();
        BeanUtils.copyProperties(request, notification, getNullPropertyNames(request));
        notificationRepository.saveAndFlush(notification);
        notificationPusher.pushNotification(request);
    }

    @Override
    public ResponseEntity<?> getAllNotificationByUser(Pageable pageable) throws Exception {
        User loggedUser = SecurityHelper.getUserFromLogged(accountRepository);
        return ApiResponseHelper.success(notificationRepository.findAllByTo(pageable, loggedUser));
    }

    @Override
    public ResponseEntity<?> read(Long id) throws Exception {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new Exception("Notification not found by id"));
        notification.setRead(true);
        return ApiResponseHelper.success();
    }

}
