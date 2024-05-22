package com.job_manager.mai.controller;

import com.job_manager.mai.contrains.TimeKeepShift;
import com.job_manager.mai.service.checkin.CheckInService;
import com.job_manager.mai.util.ApiResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/time-keeper")
@RequiredArgsConstructor
@CrossOrigin
public class CheckInController {

    private final CheckInService checkInService;

    @PostMapping("/checkin/{userId}")
    public ResponseEntity<?> checkIn(@PathVariable(name = "userId") String userId, @RequestParam(name = "shift") TimeKeepShift shift) {
        try {
            return checkInService.checkIn(userId, shift);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping("/get-all-by-user-today/{userId}")
    public ResponseEntity<?> getAllByUserToDay(@PathVariable(name = "userId") String userId, @RequestParam(name = "shift") TimeKeepShift shift) {
        try {
            return checkInService.getScheduleToday(userId);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<?> checkOut(@PathVariable(name = "userId") String userId, @RequestParam(name = "shift") TimeKeepShift shift) {
        try {
            return checkInService.checkout(userId, shift);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable) {
        try {
            return checkInService.getAll(pageable);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping("/by-user/{id}")
    public ResponseEntity<?> getByUser(Pageable pageable, @PathVariable(name = "id") String userId) {
        try {
            return checkInService.getByUser(pageable, userId);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping("/by-user-manager/{id}")
    public ResponseEntity<?> getByUserManager(Pageable pageable, @PathVariable(name = "id") String userId) {
        try {
            return checkInService.getByUserManager(pageable, userId);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }
}
