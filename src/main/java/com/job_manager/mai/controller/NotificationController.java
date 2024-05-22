package com.job_manager.mai.controller;

import com.job_manager.mai.service.notification.NotificationService;
import com.job_manager.mai.util.ApiResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@CrossOrigin
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/by-user")
    public ResponseEntity<?> getAllByUser(Pageable pageable) {
        try {
            return notificationService.getAllNotificationByUser(pageable);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> readNotification(@PathVariable(name = "id") Long id) {
        try {
            return notificationService.read(id);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }
}
